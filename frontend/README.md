# Frontend Butaca

Frontend React separado del backend Spring Boot.

## Estructura

- `src/pages/`: pantallas completas.
- `src/components/`: piezas reutilizables de interfaz.
- `src/services/`: llamadas a la API y mapeo de datos.
- `src/data/`: datos mock para trabajar sin backend.
- `src/utils/`: funciones auxiliares.
- `src/styles/`: estilos globales.

## Componentes clave para modificar rapido

- `src/components/layout/Navbar.jsx`: barra superior.
- `src/components/home/HeroSection.jsx`: bloque principal de la home.
- `src/components/events/EventCard.jsx`: tarjeta de evento.
- `src/pages/EventDetailPage.jsx`: pagina de detalle.
- `src/pages/CheckoutPage.jsx`: flujo de compra.
- `src/pages/LoginPage.jsx`: acceso de cuentas.
- `src/services/apiClient.js`: URL base del backend.
- `src/services/eventService.js`: endpoints de eventos.
- `src/services/authService.js`: login de cuentas.

## Integracion con Spring Boot

1. Cambia `useMockData` a `false` en `src/services/eventService.js`.
2. Ajusta `API_BASE_URL` en `src/services/apiClient.js`.
3. Implementa estos endpoints en Spring Boot:
   - `GET /api/eventos`
   - `GET /api/eventos/{id}`
   - `POST /api/pedidos`
   - `POST /api/auth/login`

## Login actual

- Existe una pantalla en `src/pages/LoginPage.jsx`.
- La sesion se guarda en `localStorage` usando `src/utils/authStorage.js`.
- La barra superior muestra el nombre del usuario autenticado y el tipo de cuenta.
- Por defecto, `src/services/authService.js` consume `POST /api/auth/login`.
- Si necesitas una demo sin backend, puedes poner `useMockAuth = true` en `src/services/authService.js`.

## Ejecutar

```bash
npm install
npm run dev
```

## Requisitos de entorno

- Node.js `20.19+`, `22.12+` o superior
- El proyecto ya no queda fijado a Node 20 exclusivamente
- `vite` y `@vitejs/plugin-react` fueron actualizados para trabajar con versiones modernas de Node
- El proyecto valida la version de Node antes de instalar o ejecutar Vite

## Stack actualizado

- `react` y `react-dom`: linea `19.x`
- `vite`: linea `8.x`
- `@vitejs/plugin-react`: linea `5.x`

## Versiones aplicadas

- `react`: `^19.2.0`
- `react-dom`: `^19.2.0`
- `vite`: `^8.0.8`
- `@vitejs/plugin-react`: `^5.0.2`
- compatibilidad de Node declarada: `>=20.19.0 || >=22.12.0`

## Motivo de la actualizacion

- el proyecto estaba fijado artificialmente a Node 20 por un chequeo local,
- se ajusto el stack para aceptar versiones modernas de Node,
- se actualizaron dependencias base para alinear frontend con un entorno mas actual y compatible.

Si vienes de un entorno anterior, ejecuta:

```bash
rm -rf node_modules package-lock.json
npm install
```
