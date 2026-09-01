[CmdletBinding()]
param(
    [ValidateRange(1, 65535)][int]$BackendPort = 0
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$projectRoot = (Resolve-Path -LiteralPath (Join-Path $PSScriptRoot "..")).Path
$frontendDirectory = Join-Path $projectRoot "frontend"
$mavenWrapper = Join-Path $projectRoot "mvnw.cmd"

function Get-AvailableBackendPort {
    param([int]$RequestedPort)
    if ($RequestedPort -gt 0) {
        return $RequestedPort
    }
    $p8080 = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
    if (-not $p8080) {
        return 8080
    }
    Write-Host "Puerto 8080 ocupado, usando puerto alternativo 8099 para el backend..." -ForegroundColor Yellow
    return 8099
}

$backendPort = Get-AvailableBackendPort -RequestedPort $BackendPort
$frontendPort = 5173
$apiUrl = "http://localhost:$backendPort/api"
$logDirectory = Join-Path $projectRoot "target\demo-logs"


function Assert-CommandAvailable {
    param(
        [Parameter(Mandatory = $true)][string]$Name,
        [Parameter(Mandatory = $true)][string]$Help
    )

    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        throw "Falta '$Name'. $Help"
    }
}

function Assert-PortAvailable {
    param([Parameter(Mandatory = $true)][int]$Port)

    $listener = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
    if ($listener) {
        throw "El puerto $Port ya esta ocupado. Cerra la aplicacion que lo usa y volve a ejecutar la demo."
    }
}

function Wait-ForEndpoint {
    param(
        [Parameter(Mandatory = $true)][string]$Url,
        [Parameter(Mandatory = $true)][System.Diagnostics.Process]$Process,
        [Parameter(Mandatory = $true)][string]$Name
    )

    for ($attempt = 1; $attempt -le 90; $attempt++) {
        if ($Process.HasExited) {
            throw "$Name termino antes de quedar listo. Revisa los logs en $logDirectory."
        }

        try {
            Invoke-RestMethod -Uri $Url -TimeoutSec 1 | Out-Null
            return
        }
        catch {
            Start-Sleep -Seconds 1
        }
    }

    throw "$Name no respondio dentro de 90 segundos. Revisa los logs en $logDirectory."
}

function Stop-ProcessTree {
    param([Parameter(Mandatory = $true)][int]$RootProcessId)

    $childProcesses = Get-CimInstance Win32_Process -Filter "ParentProcessId = $RootProcessId" -ErrorAction SilentlyContinue
    foreach ($childProcess in $childProcesses) {
        Stop-ProcessTree -RootProcessId $childProcess.ProcessId
    }

    Stop-Process -Id $RootProcessId -Force -ErrorAction SilentlyContinue
}

function Get-PnpmCommand {
    if (Get-Command corepack.cmd -ErrorAction SilentlyContinue) {
        return @{ File = "corepack.cmd"; Prefix = @("pnpm") }
    }
    if (Get-Command pnpm.cmd -ErrorAction SilentlyContinue) {
        return @{ File = "pnpm.cmd"; Prefix = @() }
    }
    return @{ File = "npx.cmd"; Prefix = @("pnpm") }
}

$pnpmCmd = Get-PnpmCommand

Assert-CommandAvailable -Name "java" -Help "Instala Java 17 o superior y configura JAVA_HOME."
Assert-CommandAvailable -Name "node" -Help "Instala Node.js."
Assert-PortAvailable -Port $backendPort
Assert-PortAvailable -Port $frontendPort

$viteCommand = Join-Path $frontendDirectory "node_modules\.bin\vite.cmd"
if (-not (Test-Path -LiteralPath $viteCommand)) {
    Write-Host "Instalando dependencias frontend..."
    Push-Location $frontendDirectory
    try {
        & $pnpmCmd.File @($pnpmCmd.Prefix + @("install", "--frozen-lockfile"))
        if ($LASTEXITCODE -ne 0) {
            throw "Instalacion de dependencias fallo con codigo $LASTEXITCODE."
        }
    }
    finally {
        Pop-Location
    }
}

New-Item -ItemType Directory -Path $logDirectory -Force | Out-Null
$backendOutputLog = Join-Path $logDirectory "backend.out.log"
$backendErrorLog = Join-Path $logDirectory "backend.err.log"
$frontendOutputLog = Join-Path $logDirectory "frontend.out.log"
$frontendErrorLog = Join-Path $logDirectory "frontend.err.log"

$previousApiUrl = $env:VITE_API_BASE_URL
$env:VITE_API_BASE_URL = $apiUrl
$backendProcess = $null
$frontendProcess = $null

try {
    Write-Host "Iniciando backend con H2 en memoria (puerto $backendPort)..."
    $backendProcess = Start-Process `
        -FilePath $mavenWrapper `
        -ArgumentList @("spring-boot:run", "-Dspring-boot.run.profiles=demo", "-Dspring-boot.run.arguments=--server.port=$backendPort") `
        -WorkingDirectory $projectRoot `
        -WindowStyle Hidden `
        -PassThru `
        -RedirectStandardOutput $backendOutputLog `
        -RedirectStandardError $backendErrorLog

    Wait-ForEndpoint -Url "$apiUrl/eventos" -Process $backendProcess -Name "El backend"

    Write-Host "Iniciando frontend (puerto $frontendPort)..."
    $frontendArgs = @($pnpmCmd.Prefix + @("run", "dev", "--host", "127.0.0.1", "--port", "$frontendPort"))
    $frontendProcess = Start-Process `
        -FilePath $pnpmCmd.File `
        -ArgumentList $frontendArgs `
        -WorkingDirectory $frontendDirectory `
        -WindowStyle Hidden `
        -PassThru `
        -RedirectStandardOutput $frontendOutputLog `
        -RedirectStandardError $frontendErrorLog

    Wait-ForEndpoint -Url "http://127.0.0.1:$frontendPort" -Process $frontendProcess -Name "El frontend"

    Write-Host ""
    Write-Host "Butaca esta listo para presentar" -ForegroundColor Green

    Write-Host "Frontend:   http://localhost:$frontendPort"
    Write-Host "API:        $apiUrl"
    Write-Host "Consola H2: http://localhost:$backendPort/h2-console"
    Write-Host "JDBC H2:    jdbc:h2:mem:butaca_demo | usuario: sa | password: vacio"
    Write-Host ""
    Write-Host "Usuarios demo:"
    Write-Host "  admin@butaca.com / admin123"
    Write-Host "  org@butaca.com / org123"
    Write-Host "  cliente@butaca.com / cliente123"
    Write-Host ""
    Write-Host "Logs: $logDirectory"
    Write-Host "Presiona Ctrl+C para cerrar backend y frontend."

    while (-not $backendProcess.HasExited -and -not $frontendProcess.HasExited) {
        Start-Sleep -Seconds 1
    }

    throw "Uno de los procesos termino inesperadamente. Revisa los logs en $logDirectory."
}
finally {
    Write-Host ""
    Write-Host "Cerrando la demo..."

    if ($frontendProcess -and -not $frontendProcess.HasExited) {
        Stop-ProcessTree -RootProcessId $frontendProcess.Id
    }
    if ($backendProcess -and -not $backendProcess.HasExited) {
        Stop-ProcessTree -RootProcessId $backendProcess.Id
    }

    $env:VITE_API_BASE_URL = $previousApiUrl
}
