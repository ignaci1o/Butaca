#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
FRONTEND_DIR="$ROOT_DIR/frontend"
API_URL="${VITE_API_BASE_URL:-http://localhost:8080/api}"
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

require_command java "Instala Java 17: sudo apt update && sudo apt install openjdk-17-jdk"
require_command pnpm "Activalo con: corepack enable && corepack prepare pnpm@11.3.0 --activate"

if [ ! -d "$FRONTEND_DIR/node_modules" ]; then
  echo "Instalando dependencias frontend con pnpm..."
  (cd "$FRONTEND_DIR" && pnpm install --frozen-lockfile)
fi

pids=()

cleanup() {
  echo
  echo "Cerrando procesos de demo..."
  for pid in "${pids[@]}"; do
    if kill -0 "$pid" >/dev/null 2>&1; then
      kill "$pid" >/dev/null 2>&1 || true
    fi
  done
}

trap cleanup EXIT INT TERM

echo "Iniciando Butaca en modo demo"
echo "Backend:  http://localhost:8080"
echo "API:      $API_URL"
echo "H2:       http://localhost:8080/h2-console"
echo "Frontend: http://localhost:5173"
echo
echo "Usuarios demo:"
echo "  admin@butaca.com / admin123"
echo "  org@butaca.com / org123"
echo "  cliente@butaca.com / cliente123"
echo

(
  cd "$ROOT_DIR"
  ./mvnw "${MAVEN_ARGS[@]}" spring-boot:run -Dspring-boot.run.profiles=demo
) &
pids+=("$!")

(
  cd "$FRONTEND_DIR"
  VITE_API_BASE_URL="$API_URL" pnpm run dev --host 0.0.0.0
) &
pids+=("$!")

wait -n "${pids[@]}"
