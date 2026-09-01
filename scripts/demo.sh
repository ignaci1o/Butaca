#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
FRONTEND_DIR="$ROOT_DIR/frontend"

# Puerto por defecto
BACKEND_PORT="${SERVER_PORT:-8080}"
FRONTEND_PORT="${FRONTEND_PORT:-5173}"

# Detectar si el puerto 8080 está ocupado y cambiar a 8099 si aplica
if command -v lsof >/dev/null 2>&1; then
  if lsof -Pi :"$BACKEND_PORT" -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "Puerto $BACKEND_PORT ocupado, utilizando puerto alternativo 8099..."
    BACKEND_PORT=8099
  fi
elif command -v nc >/dev/null 2>&1; then
  if nc -z 127.0.0.1 "$BACKEND_PORT" 2>/dev/null; then
    echo "Puerto $BACKEND_PORT ocupado, utilizando puerto alternativo 8099..."
    BACKEND_PORT=8099
  fi
fi

API_URL="${VITE_API_BASE_URL:-http://localhost:$BACKEND_PORT/api}"
MAVEN_ARGS=()

if [ -n "${MAVEN_REPO_LOCAL:-}" ]; then
  MAVEN_ARGS+=("-Dmaven.repo.local=$MAVEN_REPO_LOCAL")
fi

require_command() {
  local command_name="$1"
  local install_hint="$2"

  if ! command -v "$command_name" >/dev/null 2>&1; then
    echo "Falta '$command_name'. $install_hint" >&2
    exit 1
  fi
}

require_command java "Instala Java 17 o superior (en Mac: brew install openjdk@17)."
require_command node "Instala Node.js (en Mac: brew install node)."

if command -v pnpm >/dev/null 2>&1; then
  PNPM_CMD=(pnpm)
elif command -v corepack >/dev/null 2>&1; then
  PNPM_CMD=(corepack pnpm)
elif command -v npx >/dev/null 2>&1; then
  PNPM_CMD=(npx pnpm)
else
  echo "Falta pnpm. Se recomienda instalar Node.js con npm/npx incluido." >&2
  exit 1
fi

if [ ! -d "$FRONTEND_DIR/node_modules" ]; then
  echo "Instalando dependencias del frontend..."
  (cd "$FRONTEND_DIR" && "${PNPM_CMD[@]}" install --frozen-lockfile)
fi

pids=()
cleanup_done=false

cleanup() {
  if [ "$cleanup_done" = true ]; then
    return
  fi
  cleanup_done=true

  echo
  echo "Cerrando procesos de Butaca..."
  for pid in "${pids[@]}"; do
    if kill -0 "$pid" >/dev/null 2>&1; then
      kill "$pid" >/dev/null 2>&1 || true
    fi
  done
}

trap cleanup EXIT
trap 'cleanup; exit 130' INT
trap 'cleanup; exit 143' TERM

echo "=========================================="
echo "  Butaca - Modo Demo Local"
echo "=========================================="
echo "Frontend:   http://localhost:$FRONTEND_PORT"
echo "API REST:   $API_URL"
echo "Consola H2: http://localhost:$BACKEND_PORT/h2-console"
echo "            JDBC: jdbc:h2:mem:butaca_demo | user: sa | pass: (vacío)"
echo
echo "Cuentas de prueba:"
echo "  Organizador: org@butaca.com / org123"
echo "  Cliente:     cliente@butaca.com / cliente123"
echo "=========================================="
echo "Presiona Ctrl+C para detener ambos servidores."
echo

(
  cd "$ROOT_DIR"
  ./mvnw "${MAVEN_ARGS[@]}" spring-boot:run -Dspring-boot.run.profiles=demo -Dspring-boot.run.arguments="--server.port=$BACKEND_PORT"
) &
pids+=("$!")

(
  cd "$FRONTEND_DIR"
  VITE_API_BASE_URL="$API_URL" "${PNPM_CMD[@]}" run dev --host 0.0.0.0 --port "$FRONTEND_PORT"
) &
pids+=("$!")

wait -n "${pids[@]}"
