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
- `src/services/apiClient.js`: URL base del backend.
- `src/services/eventService.js`: endpoints de eventos.

## Integracion con Spring Boot

1. Cambia `useMockData` a `false` en `src/services/eventService.js`.
2. Ajusta `API_BASE_URL` en `src/services/apiClient.js`.
3. Implementa estos endpoints en Spring Boot:
   - `GET /api/eventos`
   - `GET /api/eventos/{id}`
   - `POST /api/pedidos`

## Ejecutar

```bash
npm install
npm run dev
```
