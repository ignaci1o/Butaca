# Frontend — Butaca

> React + Vite · Tema oscuro premium · Conectado a Spring Boot + PostgreSQL

---

## Estado actual del proyecto

| Área | Estado |
|---|---|
| Diseño visual | ✅ Tema oscuro premium (v2) |
| Conexión a API REST | ✅ Activa (mocks desactivados) |
| Autenticación | ✅ Login + Registro contra backend |
| Panel Organizador | ✅ Crear, listar y publicar eventos |
| Compra de entradas | ✅ Checkout transaccional |
| Build de producción | ✅ Sin errores (`vite build`) |

---

## Diseño — v2 (Abril 2026)

El frontend fue rediseñado completamente con un sistema visual unificado:

- **Paleta oscura profunda** — fondo `#080c12`, degradados sutiles naranja/teal como acentos de fondo.
- **Glassmorphism** — todas las cards usan `backdrop-filter: blur()` con bordes translúcidos (`rgba`).
- **Tipografía Inter** — cargada desde Google Fonts, pesos 400–900 para jerarquía clara.
- **Variables CSS centralizadas** — colores, sombras y radii definidos en `:root` en `global.css`.
- **Micro-animaciones** — cards se elevan al hover, imagen hace zoom, flecha del link se desplaza.
- **Inputs con foco naranja** — `box-shadow` glow en todos los campos de formulario.

### Archivos modificados en el rediseño

| Archivo | Cambio |
|---|---|
| `src/styles/global.css` | Reescritura completa del sistema de diseño |
| `index.html` | Agregado Google Fonts (Inter + DM Serif Display) |
| `src/components/layout/Navbar.jsx` | Avatar con inicial, links con estado activo |
| `src/components/layout/Footer.jsx` | Grid 4 columnas, referencia de endpoints de API |
| `src/components/home/HeroSection.jsx` | Fallback cuando no hay evento destacado |
| `src/components/events/EventCard.jsx` | Hover con elevación y zoom de imagen |

---

## Estructura del código

```
src/
├── pages/               # Pantallas completas
│   ├── HomePage.jsx
│   ├── SearchPage.jsx
│   ├── EventDetailPage.jsx
│   ├── CheckoutPage.jsx
│   ├── ConfirmationPage.jsx
│   ├── LoginPage.jsx
│   ├── RegisterPage.jsx
│   └── OrganizerDashboard.jsx
├── components/
│   ├── layout/          # Navbar, Footer, AppShell
│   ├── home/            # HeroSection, CategoryTabs
│   ├── events/          # EventCard, EventGrid
│   ├── checkout/        # Componentes del carrito
│   └── ui/              # Button, SectionTitle, etc.
├── services/            # Clientes de API
│   ├── apiClient.js     # fetch base → http://localhost:8080/api
│   ├── authService.js   # login, register
│   ├── eventService.js  # getAllEvents, getEventById
│   └── orderService.js  # createOrder
├── utils/
│   ├── authStorage.js   # Persistencia de sesión en localStorage
│   └── formatters.js    # formatDate, formatPrice
├── data/
│   └── mockEvents.js    # Datos de prueba (solo fallback)
└── styles/
    └── global.css       # Sistema de diseño completo
```

---

## Flujo de interacción: Pantallas → API → Base de datos

### 🎟️ 1. Exploración de Eventos (`HomePage`, `SearchPage`, `EventDetailPage`)
- **Acción:** El usuario navega y filtra eventos.
- **API:** `GET /api/eventos` · `GET /api/eventos/{id}`
- **Backend:** `EventoController` → `EventoService`
- **DB:** Tabla `eventos` — filtra por `estado_publicacion_evento = 'PUBLICADO'`

### 🔐 2. Autenticación (`LoginPage`, `RegisterPage`)
- **Acción:** El usuario se registra o inicia sesión.
- **API:**
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- **Backend:** `AuthController` → `AuthService`
- **DB:** Tabla `usuarios`
  - Registro: `INSERT` con `rol_usuario` y `password_usuario` encriptada con BCrypt.
  - Login: `SELECT` por `email_usuario`. Devuelve objeto de usuario que se guarda en `localStorage`.

### 🛒 3. Compra de Entradas (`CheckoutPage`, `ConfirmationPage`)
- **Acción:** Un cliente logueado compra una entrada.
- **API:** `POST /api/compras` (envía `evento_id` + `usuario_id`)
- **Backend:** `CompraController` → `CompraService` (transaccional)
- **DB:**
  1. `SELECT` en `eventos` — verifica `cupos_disponible_evento > 0`
  2. `UPDATE` en `eventos` — resta `-1` a `cupos_disponible_evento`
  3. `INSERT` en `entradas` — genera `codigo_unico_entrada` (UUID) en estado `CONFIRMADA`

### 📊 4. Panel Organizador (`OrganizerDashboard`)
- **Acción:** Un `ORGANIZADOR` o `ADMIN` gestiona sus eventos.
- **API:**
  - `GET /api/eventos/organizador/{id}` — lista propios (borrador + publicado)
  - `POST /api/eventos` — crea nuevo evento en estado `BORRADOR`
  - `PUT /api/eventos/{id}/publicar` — cambia estado a `PUBLICADO`
- **Backend:** `EventoController` → `EventoService`
- **DB:** Tabla `eventos` — INSERT y UPDATE según la acción.

---

## Integración con Spring Boot

Los mocks están **desactivados**. El frontend consume directamente el backend en:

```
http://localhost:8080/api
```

Verificá en `src/services/`:

```js
// eventService.js
export const useMockData = false;

// orderService.js
export const useMockOrders = false;

// authService.js
export const useMockAuth = false;
```

---

## Cómo ejecutar

```bash
# Instalar dependencias
npm install

# Servidor de desarrollo (http://localhost:5173)
npm run dev

# Build de producción
npm run build
```

**Requisitos:**
- Node.js `>=20.19.0` o `>=22.12.0`
- React `19.x` · React Router `6.x` · Vite `8.x`

> ⚠️ El backend Spring Boot debe estar corriendo en el puerto `8080` antes de usar el frontend.
