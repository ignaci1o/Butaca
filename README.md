# Butaca

Sistema web de gestión y venta de entradas para eventos. El proyecto evolucionó desde una idea MVC clásica hacia una arquitectura desacoplada con backend REST en Spring Boot y frontend separado en React.

Este README refleja el estado real del repositorio al día de hoy. El dominio principal ya se encuentra completamente persistido en PostgreSQL y expuesto vía API REST.

## 1. Objetivo del proyecto

Butaca busca resolver un flujo básico de ticketera digital para organizadores pequeños y eventos locales:

- publicar eventos,
- listar y buscar eventos,
- vender o reservar entradas,
- controlar cupos,
- validar entradas por código único,
- manejar cuentas con distintos roles.

## 2. Arquitectura actual

### Backend

- Java `17`
- Spring Boot `4.0.5`
- Spring Web MVC
- Spring Data JPA
- Validaciones con Jakarta Validation
- Hash de password con BCrypt
- PostgreSQL para persistencia de todas las entidades

### Frontend

- React `19.2.0`
- Vite `8.0.8`
- React Router DOM `6.30.1`

### Enfoque de integración

- Backend: expone endpoints REST bajo `/api`
- Frontend: consume la API desde `http://localhost:8080/api`
- CORS habilitado para `http://localhost:5173` y `http://127.0.0.1:5173`

## 3. Estado actual real del proyecto

Hoy el proyecto tiene el flujo principal completamente conectado a base de datos real.

### Módulos persistidos en PostgreSQL

El sistema utiliza Spring Data JPA para todo su dominio:

- Entidad `Usuario`: maneja autenticación, encriptación con BCrypt y roles.
- Entidad `Evento`: eventos publicados por los organizadores con control de cupos.
- Entidad `Entrada`: tickets generados tras una compra, vinculados a un código único UUID y con estados de validación.
- Relaciones establecidas entre Organizador, Evento y Entrada (Cliente).

### Estado real del frontend

El frontend consume la API REST real provista por el backend. El uso de mocks de datos locales fue desactivado completamente:

- Login conectado al backend real y guardado en `localStorage`.
- Eventos consumidos directamente desde `/api/eventos`.
- Checkout y compras consumidas enviando datos al endpoint `/api/compras` y recibiendo códigos de reserva reales.

## 4. Últimos cambios incorporados

Los cambios más recientes que ya impactan el proyecto son estos:

1. Se refactorizaron las entidades `Evento` y `Entrada` para usar Spring Data JPA con la nomenclatura estricta solicitada (`id_usuario`, `titulo_evento`, etc.).
2. Se eliminaron los mapas en memoria y se reemplazaron por `JpaRepository`.
3. Se estructuró la lógica de negocio en módulos dedicados: `EventoService`, `EntradaService` y `CompraService`.
4. Se expuso toda la lógica mediante Controladores REST con DTOs de Request y Response usando anotaciones `@JsonProperty` para coincidir con la nomenclatura de la base de datos.
5. Se implementó un `GlobalExceptionHandler` para atrapar excepciones de forma limpia y retornar códigos HTTP apropiados.
6. Se añadió un `DataLoader` que inyecta cuentas base (Admin, Organizador, Cliente) y eventos de ejemplo de forma automática.
7. Se actualizó el frontend (`eventService.js` y `orderService.js`) para apagar los mocks y consumir los endpoints.

## 5. Endpoints disponibles hoy

### Módulo 1: Auth
- `POST /api/auth/register` (Crea usuario nuevo)
- `POST /api/auth/login` (Devuelve token/información)

### Módulo 2 y 5: Eventos
- `POST /api/eventos` (Alta de evento por Organizador)
- `GET /api/eventos` (Listado público de eventos PUBLICADOS)
- `GET /api/eventos/{id}` (Detalle de un evento)
- `PUT /api/eventos/{id}` (Editar evento)
- `GET /api/eventos/organizador/{id}` (Eventos de un organizador)
- `GET /api/eventos/{id}/entradas` (Entradas vendidas para un evento)
- `PUT /api/eventos/{id}/publicar` (Cambiar estado a PUBLICADO)

### Módulo 3: Entradas
- `GET /api/entradas/validar/{codigo}` (Validar código único UUID)
- `PUT /api/entradas/{id}/usar` (Marcar entrada como USADA)
- `GET /api/entradas/usuario/{id}` (Listar compras de un usuario)

### Módulo 4: Compras
- `POST /api/compras` (Procesar compra, descontar cupo y emitir UUID)
- `GET /api/compras/usuario/{id}` (Historial de compras)

## 6. Estructura actual del repositorio

### Backend

- `src/main/java/com/demo/butaca/controller`: controladores REST y `GlobalExceptionHandler`.
- `src/main/java/com/demo/butaca/service`: servicios de negocio segmentados (`Auth`, `Evento`, `Entrada`, `Compra`).
- `src/main/java/com/demo/butaca/repository`: Repositorios Spring Data JPA.
- `src/main/java/com/demo/butaca/model`: Entidades JPA de dominio.
- `src/main/java/com/demo/butaca/dto`: Contratos JSON de entrada y salida con nomenclatura forzada.
- `src/main/java/com/demo/butaca/config`: Contiene el `DataLoader` que inicializa datos semilla.
- `src/main/resources/application.properties`: configuración de datasource y JPA.

### Frontend

- `frontend/src/pages`: pantallas principales.
- `frontend/src/components`: componentes de interfaz.
- `frontend/src/services`: acceso a la API REST real.
- `frontend/src/utils`: almacenamiento local y utilidades.

## 7. Configuración de PostgreSQL

El backend espera estas variables de entorno:

- `DB_USERNAME`
- `DB_PASSWORD`

Configuración actual:

```properties
spring.datasource.url=jdbc:postgresql://100.73.210.97:5433/db_proyectos
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

## 8. Cómo levantar el proyecto

### Backend

En PowerShell:

```powershell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.jvmArguments=-Duser.timezone=UTC'
```

En Git Bash o Linux/Mac:

```bash
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password
./mvnw clean compile
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Compatibilidad declarada de Node:

- `>=20.19.0 || >=22.12.0`

## 9. Próximos pasos a futuro

Dado que el motor principal ya está operando sobre base de datos y endpoints reales, los próximos pasos recomendados son:

1. **Seguridad Avanzada**: Migrar la sesión estática o el almacenamiento básico en `localStorage` hacia un esquema con **JWT (JSON Web Tokens)** o Spring Security robusto que proteja las rutas mediante filtros reales.
2. **Registro de Usuarios en UI**: Conectar el formulario visual de registro en el frontend con el endpoint `/api/auth/register`.
3. **Módulo de Organizadores Completo**: Construir las pantallas de Dashboard en React donde un Organizador pueda crear un evento visualmente y publicarlo.
