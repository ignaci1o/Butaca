# Butaca

Sistema web de gestion y venta de entradas para eventos. El proyecto evoluciono desde una idea MVC clasica hacia una arquitectura desacoplada con backend REST en Spring Boot y frontend separado en React.

Este README refleja el estado real del repositorio al dia de hoy, con foco en lo que ya funciona y en lo que falta para seguir avanzando sobre base de datos, frontend y conexion completa con backend.

## 1. Objetivo del proyecto

Butaca busca resolver un flujo basico de ticketera digital para organizadores pequenos y eventos locales:

- publicar eventos,
- listar y buscar eventos,
- vender o reservar entradas,
- controlar cupos,
- validar entradas por codigo unico,
- manejar cuentas con distintos roles.

## 2. Arquitectura actual

### Backend

- Java `17`
- Spring Boot `4.0.5`
- Spring Web MVC
- Spring Data JPA
- Validaciones con Jakarta Validation
- Hash de password con BCrypt
- PostgreSQL para el modulo de usuarios

### Frontend

- React `19.2.0`
- Vite `8.0.8`
- React Router DOM `6.30.1`

### Enfoque de integracion

- Backend: expone y va a exponer endpoints REST bajo `/api`
- Frontend: consume la API desde `http://localhost:8080/api`
- CORS habilitado para `http://localhost:5173` y `http://127.0.0.1:5173`

## 3. Estado actual real del proyecto

Hoy el proyecto esta dividido en dos niveles de madurez distintos.

### Modulos ya conectados a base de datos

El modulo de autenticacion ya usa PostgreSQL + JPA:

- entidad `Usuario`,
- enum `RolUsuario`,
- `UsuarioRepository` con JPA,
- `AuthService`,
- `AuthController`,
- password hasheada con BCrypt.

### Modulos que todavia no estan persistidos ni expuestos como API REST

El flujo principal de negocio de ticketera todavia esta incompleto en terminos de integracion:

- `Evento` y `Entrada` siguen siendo clases simples, no entidades JPA,
- `EventoRepository` y `EntradaRepository` siguen en memoria con `Map`,
- `TicketService` tiene logica de crear evento, comprar entrada y validar entrada,
- no existen controladores REST para eventos, entradas ni compras,
- al reiniciar el backend, los datos en memoria se pierden.

### Estado real del frontend

El frontend ya tiene navegacion, pantallas y estructura de servicios, pero la integracion es parcial:

- login conectado al backend real,
- sesion guardada en `localStorage`,
- navbar mostrando nombre y rol del usuario autenticado,
- eventos consumidos desde mocks,
- checkout y pedidos consumidos desde mocks.

En otras palabras:

- autenticacion: conectada,
- eventos: simulados,
- compras: simuladas,
- validacion de entradas: todavia no conectada al frontend.

## 4. Ultimos cambios incorporados en el repositorio

Los cambios mas recientes que ya impactan el proyecto son estos:

1. se elimino la capa MVC con vistas renderizadas en servidor y se adopto frontend React separado,
2. se agrego el flujo inicial de autenticacion con registro, login y roles,
3. se preparo PostgreSQL y JPA para persistencia real,
4. se habilito CORS para que el frontend en Vite pueda hablar con Spring Boot,
5. se actualizo el stack del frontend a React 19 + Vite 8 y se amplio compatibilidad de Node.

## 5. Objetivos claros de la siguiente etapa

Para seguir programando sin desorden, conviene tomar como objetivos inmediatos los siguientes:

### Objetivo 1: completar la base de datos del dominio principal

Pasar de una persistencia parcial a una persistencia coherente del sistema.

Resultado esperado:

- usuarios, eventos y entradas guardados en PostgreSQL,
- sin perdida de datos al reiniciar,
- relaciones claras entre organizador, evento y entrada.

### Objetivo 2: cerrar la API REST del backend

Exponer por endpoints HTTP la logica que ya existe y la que falta implementar.

Resultado esperado:

- frontend sin mocks para eventos y compras,
- contrato API estable,
- validaciones y errores consistentes.

### Objetivo 3: perfeccionar el frontend sobre datos reales

Una vez que la API exista, refinar UX, estados de carga, errores y flujos de usuario.

Resultado esperado:

- login, listado de eventos, detalle, checkout y confirmacion conectados al backend,
- interfaz mas robusta,
- separacion clara entre vistas publicas y vistas de organizador/admin.

## 6. Lista de cambios necesarios para el proyecto

Esta es la lista de trabajo recomendada para dejar el proyecto alineado con el objetivo final.

### Base de datos

- convertir `Evento` en entidad JPA,
- convertir `Entrada` en entidad JPA,
- definir relaciones entre `Usuario`, `Evento` y `Entrada`,
- decidir si el organizador sera una referencia a `Usuario`,
- agregar campos faltantes del dominio de eventos: descripcion, ciudad, recinto, precio base o estructura de precios, estado de publicacion, fecha y hora,
- agregar restricciones e indices utiles,
- revisar `ddl-auto=update` para desarrollo y definir una estrategia mas controlada para entregas futuras.

### Backend REST

- crear `EventoController`,
- crear endpoints para alta, listado y detalle de eventos,
- crear endpoint de compra o reserva de entrada,
- crear endpoint de validacion de entrada por codigo,
- estandarizar respuestas y errores,
- definir DTOs de request/response para eventos y compras,
- mover reglas de negocio faltantes a servicios dedicados si el modulo crece.

### Seguridad y cuentas

- mantener el flujo actual de login como base,
- agregar registro visual en el frontend,
- evaluar incorporacion de Spring Security,
- definir si la siguiente etapa va a usar JWT o sesion simple,
- proteger endpoints por rol (`ADMIN`, `ORGANIZADOR`, `CLIENTE`).

### Frontend

- apagar mocks en `eventService` y `orderService` cuando exista API real,
- conectar home, busqueda y detalle con datos del backend,
- conectar checkout con compra real,
- mostrar estados de loading, empty y error,
- agregar formulario de registro,
- preparar vistas para organizadores: crear y administrar eventos,
- revisar componentes y estilos para mejorar claridad visual y consistencia de flujos.

### Calidad y mantenimiento

- agregar tests de servicios y controladores,
- documentar contratos de API,
- definir datos semilla para desarrollo,
- revisar configuracion por ambientes,
- separar configuracion sensible mediante variables de entorno.

## 7. Orden recomendado de implementacion

Para no bloquear frontend ni backend, este orden es el mas razonable:

1. persistir `Evento` y `Entrada` en PostgreSQL,
2. exponer endpoints REST de eventos,
3. apagar mocks de eventos en el frontend,
4. exponer compra/reserva de entradas,
5. apagar mocks de checkout/pedidos,
6. agregar validacion de entradas,
7. recien despues endurecer seguridad y permisos por rol.

## 8. Endpoints disponibles hoy

Actualmente el backend expone solo autenticacion:

### `POST /api/auth/register`

Crea un usuario nuevo.

Ejemplo de body:

```json
{
  "nombre": "Leandro",
  "apellido": "Gramajo Gargiulo",
  "email": "leandro@example.com",
  "password": "clave123",
  "rol": "ORGANIZADOR"
}
```

### `POST /api/auth/login`

Valida credenciales y devuelve datos basicos del usuario.

Ejemplo de body:

```json
{
  "email": "leandro@example.com",
  "password": "clave123"
}
```

## 9. Estructura actual del repositorio

### Backend

- `src/main/java/com/demo/butaca/controller`: controladores y manejo global de errores.
- `src/main/java/com/demo/butaca/service`: logica de autenticacion y ticketera.
- `src/main/java/com/demo/butaca/repository`: JPA para usuarios e implementaciones en memoria para eventos/entradas.
- `src/main/java/com/demo/butaca/model`: modelo de dominio.
- `src/main/java/com/demo/butaca/dto`: contratos de entrada/salida.
- `src/main/resources/application.properties`: configuracion de datasource y JPA.

### Frontend

- `frontend/src/pages`: pantallas principales.
- `frontend/src/components`: componentes de interfaz.
- `frontend/src/services`: acceso a API y mocks.
- `frontend/src/utils`: almacenamiento local y utilidades.
- `frontend/src/data`: datos mock para eventos.

## 10. Configuracion de PostgreSQL

El backend espera estas variables de entorno:

- `DB_USERNAME`
- `DB_PASSWORD`

Configuracion actual:

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

## 11. Como levantar el proyecto

### Backend

En PowerShell:

```powershell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.jvmArguments=-Duser.timezone=UTC'
```

En Git Bash:

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

## 12. Riesgos tecnicos actuales

Estos puntos siguen abiertos y conviene tenerlos presentes mientras continues el desarrollo:

- el dominio principal del negocio todavia no persiste en base de datos,
- el frontend puede dar una falsa sensacion de avance porque eventos y compras siguen mockeados,
- no hay seguridad completa ni autorizacion por rol,
- la cobertura de tests es minima,
- la API todavia no tiene contrato cerrado para los modulos de eventos y compras.

## 13. Definicion de trabajo inmediato

Si el objetivo ahora es seguir programando base de datos, perfeccionar frontend y conectar todo con backend, el siguiente entregable tecnico deberia ser este:

1. modelar `Evento` y `Entrada` como entidades JPA,
2. crear endpoints REST de eventos,
3. conectar el frontend de listado y detalle a esos endpoints,
4. despues implementar compra real de entradas,
5. finalmente mejorar UX y permisos por rol.

Ese orden evita rehacer trabajo y baja la cantidad de mocks que quedan dando vueltas en el proyecto.
