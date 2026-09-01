@echo off
cd /d "%~dp0"
echo ===========================================
echo   Iniciando Butaca...
echo ===========================================
start http://localhost:8080
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=demo
pause
