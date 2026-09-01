#!/bin/bash
cd "$(dirname "$0")"
echo "==========================================="
echo "  Iniciando Butaca (Plataforma Académica)"
echo "==========================================="
echo "Requisito: Java 17 instalado."
echo ""
chmod +x ./mvnw
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo &
PID=$!
sleep 5
open http://localhost:8080
wait $PID
