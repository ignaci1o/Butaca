# Frontend Butaca

Este es el frontend en React, completamente integrado con la API REST de Spring Boot y la base de datos PostgreSQL.

## 1. Estructura del Código

- `src/pages/`: Las ventanas o pantallas completas de la aplicación.
- `src/components/`: Componentes reutilizables (Navbar, EventCard, etc.).
- `src/services/`: Clientes para la API (`apiClient.js`, `eventService.js`, `authService.js`, `orderService.js`).
- `src/utils/`: Funciones auxiliares como el manejo de sesión en `localStorage`.
- `src/styles/`: Estilos globales y específicos.

## 2. Flujo de Interacción: Ventanas vs. Base de Datos

Cada pantalla del frontend dispara interacciones específicas que recorren la red hasta Spring Boot y terminan afectando o leyendo tablas particulares en PostgreSQL. Aquí se detalla la conexión exacta:

### 🎟️ 1. Exploración y Detalle (`HomePage.jsx`, `SearchPage.jsx`, `EventDetailPage.jsx`)
- **Acción del Usuario:** El usuario entra a la página principal o busca eventos para ver su disponibilidad y detalles.
- **Llamada a la API:** `GET /api/eventos` y `GET /api/eventos/{id}`.
- **Módulo Backend:** `EventoController` ➔ `EventoService`.
- **Tabla en Base de Datos:** Lee de la tabla `eventos`. La consulta filtra exclusivamente los eventos que tienen el campo `estado_publicacion_evento = 'PUBLICADO'`.

### 🔐 2. Autenticación (`LoginPage.jsx` y `RegisterPage.jsx`)
- **Acción del Usuario:** El usuario se registra como Cliente/Productora o inicia sesión.
- **Llamadas a la API:** 
  - `POST /api/auth/register` (Registro)
  - `POST /api/auth/login` (Login)
- **Módulo Backend:** `AuthController` ➔ `AuthService`.
- **Tabla en Base de Datos:** Interacciona con la tabla `usuarios`. 
  - En el registro, hace un `INSERT` con el `rol_usuario` (CLIENTE u ORGANIZADOR) y guarda la `password_usuario` encriptada con BCrypt. 
  - En el login, hace un `SELECT` buscando por `email_usuario`.

### 🛒 3. Compra de Entradas (`CheckoutPage.jsx` y `ConfirmationPage.jsx`)
- **Acción del Usuario:** Un cliente logueado decide comprar una entrada y confirma su reserva.
- **Llamada a la API:** `POST /api/compras` enviando el `evento_id` y `usuario_id`.
- **Módulo Backend:** `CompraController` ➔ `CompraService` (100% Transaccional).
- **Tablas en Base de Datos:**
  1. Lee la tabla `eventos` para verificar que el `cupos_disponible_evento` sea mayor a 0.
  2. **Actualiza** la tabla `eventos` restando `-1` al campo `cupos_disponible_evento`.
  3. Hace un **INSERT** en la tabla `entradas` vinculando las claves foráneas del usuario y evento, y genera automáticamente un `codigo_unico_entrada` (UUID) en estado `CONFIRMADA`.

### 📊 4. Panel de Productora (`OrganizerDashboard.jsx`)
- **Acción del Usuario:** Un usuario con rol `ORGANIZADOR` o `ADMIN` entra a "Mi Panel" para ver sus eventos, crear uno nuevo o publicarlos.
- **Llamadas a la API:**
  - `GET /api/eventos/organizador/{id}`: Lee todos los eventos (Borradores y Publicados) que le pertenecen.
  - `POST /api/eventos`: Envía el formulario para crear un evento nuevo.
  - `PUT /api/eventos/{id}/publicar`: Hace público un borrador.
- **Módulo Backend:** `EventoController` ➔ `EventoService`.
- **Tabla en Base de Datos:** Todo ocurre en la tabla `eventos`. 
  - Al crear, hace un `INSERT` seteando el `estado_publicacion_evento = 'BORRADOR'`.
  - Al publicar, hace un `UPDATE` cambiando el estado a `'PUBLICADO'`.

---

## 3. Integración con Spring Boot

Asegúrate de que los mocks estén apagados en los servicios correspondientes (`src/services/`) para conectarte a la base de datos real:

1. `useMockData = false` en `eventService.js`.
2. `useMockOrders = false` en `orderService.js`.
3. `useMockAuth = false` en `authService.js`.

El `apiClient.js` está configurado para buscar el backend en `http://localhost:8080/api`.

## 4. Cómo Ejecutar

```bash
# Instalar dependencias
npm install

# Levantar servidor de desarrollo
npm run dev
```

**Requisitos de entorno:**
- Node.js `>=20.19.0 || >=22.12.0`
- React y React DOM `19.x`
- Vite `8.x`
