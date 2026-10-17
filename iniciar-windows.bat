@echo off
cd /d "%~dp0"
if "%JAVA_HOME%"=="" (
    if exist "%USERPROFILE%\.butaca-tools\jdk\bin\java.exe" (
        set "JAVA_HOME=%USERPROFILE%\.butaca-tools\jdk"
        set "PATH=%USERPROFILE%\.butaca-tools\jdk\bin;%USERPROFILE%\.butaca-tools\node;%PATH%"
    )
)
echo ===========================================
echo   Iniciando Butaca...
echo ===========================================
start http://localhost:8080
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=demo
pause
