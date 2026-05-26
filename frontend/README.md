# Frontend Butaca

Aplicacion React + Vite conectada al backend Spring Boot de Butaca.

## Requisitos

- Node.js `>=20.19.0` o `>=22.12.0` (`../.nvmrc` fija `22.20.0`).
- pnpm `>=11.3.0`.
- Backend corriendo en `http://localhost:8080`.

## Configuracion

Para la demo completa desde la raiz del repo:

```bash
../scripts/demo.sh
```

Para ejecutar solo el frontend:

```bash
cp .env.example .env
pnpm install
pnpm run dev
```

Variable disponible:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Scripts

```bash
pnpm run dev
pnpm run build
pnpm run preview
```

`pnpm run build` genera `frontend/dist/` localmente. Esa carpeta esta ignorada por Git y no forma parte del codigo fuente.

## Pantallas

- Home y busqueda de eventos publicados.
- Detalle de evento y seleccion de cantidad.
- Checkout y confirmacion con UUID de entradas.
- Login y registro contra `/api/auth`.
- Dashboard por rol:
  - `ADMIN`: metricas, categorias y ubicaciones.
  - `ORGANIZADOR`: eventos propios y compras recibidas.
  - `CLIENTE`: compras y notificaciones.

## Servicios

- `apiClient.js`: cliente HTTP base.
- `authService.js`: login, registro y normalizacion de usuario.
- `eventService.js`: eventos, categorias, ubicaciones y dashboard admin.
- `orderService.js`: compras, compras por usuario/organizador y notificaciones.

Si Vite falla por una dependencia nativa opcional de Rolldown en WSL, ejecutar de nuevo:

```bash
pnpm install
pnpm run build
```
