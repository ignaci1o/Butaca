# Butaca

Sistema web de gestion y venta de entradas para eventos.

## 1. Contexto del proyecto

Butaca se desarrolla como una version propia y acotada de una ticketera digital, orientada a pequenos organizadores y eventos locales.
El objetivo academico es construir un producto funcional, entendible y defendible, con evolucion por etapas (MVP primero, mejoras despues).

Ademas, el equipo trabaja con control remoto del repositorio en Gitea, lo que permite colaboracion, versionado y trazabilidad de cambios.

## 2. Problema a resolver

Muchos organizadores pequenos no cuentan con una herramienta simple para:

- publicar eventos,
- controlar cupos,
- registrar ventas/reservas,
- validar entradas.

Butaca busca centralizar esas operaciones en una solucion unica y clara.

## 3. Propuesta de solucion

El sistema permitira:

- crear y listar eventos,
- comprar entradas,
- generar codigo unico por entrada (UUID),
- asociar ese codigo a la entrada (base para QR),
- validar entradas por codigo, evitando doble uso.

## 4. Modelo de arquitectura adoptado

Estamos usando y proponiendo:

1. Modelo objetivo: arquitectura cliente-servidor con Backend REST API + Frontend separado.
2. Paradigma de programacion: POO (Programacion Orientada a Objetos).
3. Organizacion del backend: arquitectura por capas:

- `controller` (endpoints HTTP)
- `service` (logica de negocio)
- `repository` (persistencia, actualmente en memoria)
- `model` (entidades)

## 5. Estado actual del desarrollo

1. Base de Spring Boot funcionando correctamente.
2. Entidades, repositorios en memoria y logica principal de compra/validacion ya armadas.
3. La capa MVC con Thymeleaf fue eliminada para dejar de mezclar backend con vistas renderizadas en servidor.
4. Existe un frontend separado en React dentro de `frontend/`, con componentes, paginas y servicios preparados para integrarse con Spring Boot.
5. Todavia faltan los controladores REST (`@RestController`) para exponer la logica del backend como API JSON real.

## 6. Cambio de enfoque (importante para documentar)

- Antes: MVC clasico (backend renderiza vistas).
- Ahora: REST desacoplado (backend expone datos y reglas; frontend maneja presentacion y UX).

Esto no cambia la logica central del sistema, solo donde vive la capa de presentacion.

## 7. Usuarios del sistema

- Administrador: controla configuracion general, usuarios y operacion global.
- Organizador: crea y gestiona eventos, entradas y seguimiento basico.
- Cliente: consulta eventos y compra/reserva entradas.

## 8. Alcance inicial (MVP academico)

Incluye:

- gestion basica de eventos,
- compra/reserva de entradas,
- control de cupos/disponibilidad,
- generacion y validacion de codigo unico por entrada.

No incluye en esta etapa:

- pagos reales,
- envio de correos,
- app movil,
- mapa de asientos,
- promociones avanzadas,
- seguridad avanzada de produccion.

## 9. Tecnologias en uso actual

- Backend: Java + Spring Boot
- Frontend: React + Vite
- API: objetivo REST con JSON, aun no expuesta completamente en el backend
- Persistencia actual del backend: en memoria (para MVP)
- Control de versiones: Git + Gitea

## 10. Entorno recomendado

- Java: `17`
- Node.js: `20.x`
- npm: la version incluida con Node 20

Para trabajar sin problemas entre la PC principal y la laptop:

- usar la misma version de Java en ambas maquinas,
- usar `nvm` y respetar el archivo `.nvmrc`,
- no usar `npm install --force` ni `--legacy-peer-deps`,
- mantener `package-lock.json` versionado,
- hacer `git pull` antes de cambiar de maquina.

## 11. Proximo objetivo tecnico

Dejar Butaca en modo Backend REST limpio, documentar endpoints y conectar el frontend React con un contrato API estable.

## 12. Estructura actual del repositorio

### Backend

- `src/main/java/com/demo/butaca/model`: entidades `Evento` y `Entrada`.
- `src/main/java/com/demo/butaca/repository`: persistencia temporal en memoria con `Map`.
- `src/main/java/com/demo/butaca/service`: logica principal de crear eventos, comprar entradas y validar codigos.
- `src/main/java/com/demo/butaca/dto`: requests base para crear evento, comprar y validar.

### Frontend

- `frontend/src/pages`: pantallas principales del sitio.
- `frontend/src/components`: componentes reutilizables de interfaz.
- `frontend/src/services`: capa para consumir la API del backend o usar mocks.
- `frontend/src/data`: datos mock para trabajar mientras la API REST no este lista.
- `frontend/src/styles`: estilos globales.

## 13. Estado real de integracion

- El backend no usa base de datos todavia.
- El backend mantiene los datos en memoria y los pierde al reiniciar.
- El frontend React hoy funciona con datos mock.
- La integracion real entre frontend y backend queda como siguiente etapa inmediata.

## 14. Preparacion de PostgreSQL en el proyecto

El proyecto ya queda preparado para conectarse a PostgreSQL desde Spring Boot.

### Dependencias agregadas

En `pom.xml` se agregaron:

- `spring-boot-starter-data-jpa`
- `postgresql`

Con eso, Maven descarga automaticamente el driver JDBC y el soporte JPA/Hibernate cuando ejecutes:

```bash
./mvnw clean compile
```

o:

```bash
./mvnw spring-boot:run
```

### Configuracion agregada

En `src/main/resources/application.properties` queda configurada la conexion base:

```properties
spring.datasource.url=jdbc:postgresql://100.73.210.97:5433/db_proyectos
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
```

### Donde poner usuario y password

No se dejaron credenciales fijas dentro del repo. Tenes que definir estas variables de entorno antes de levantar el backend:

- `DB_USERNAME`
- `DB_PASSWORD`

Ejemplo en Linux/macOS o Git Bash:

```bash
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password
./mvnw spring-boot:run
```

Ejemplo en PowerShell:

```powershell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"
./mvnw spring-boot:run
```

### Importante

Esto deja listo el proyecto para conectarse a PostgreSQL, pero el backend todavia no persiste en la base real porque los repositorios actuales siguen siendo en memoria.

Para completar la migracion faltan estos pasos:

- convertir `Evento` y `Entrada` en entidades JPA (`@Entity`),
- reemplazar los repositorios con `JpaRepository`,
- crear los controladores REST que el frontend espera,
- desactivar los mocks del frontend cuando la API este disponible.

## 15. Modulo de cuentas inicial

Se agrego una primera base real para cuentas de usuario usando PostgreSQL y JPA.

### Estructura agregada

- Entidad `Usuario`
- Enum `RolUsuario` con valores `ADMIN`, `ORGANIZADOR`, `CLIENTE`
- `UsuarioRepository` con JPA
- `AuthService` para registro e inicio de sesion
- `AuthController` con endpoints REST

### Tabla que crea Hibernate

Con `spring.jpa.hibernate.ddl-auto=update`, al levantar la app se crea la tabla `usuarios` con estos campos:

- `id`
- `nombre`
- `apellido`
- `email` unico
- `password_hash`
- `rol`
- `activo`
- `created_at`

### Endpoints disponibles

#### Registro

`POST /api/auth/register`

Body de ejemplo:

```json
{
  "nombre": "Leandro",
  "apellido": "Gramajo Gargiulo",
  "email": "leandro@example.com",
  "password": "clave123",
  "rol": "ORGANIZADOR"
}
```

#### Login

`POST /api/auth/login`

Body de ejemplo:

```json
{
  "email": "leandro@example.com",
  "password": "clave123"
}
```

### Estado actual del modulo

- La contraseña se guarda hasheada con BCrypt.
- Todavia no hay JWT, sesiones ni Spring Security completo.
- El login valida email y password, pero no genera token todavia.
- Esto alcanza como base academica inicial para continuar con eventos y compras.

## 16. Registro de avance: login, roles y primer flujo de cuentas

### Que se agrego

Se incorporo una primera implementacion completa del flujo de cuentas para el entregable:

- tabla `usuarios` generada por JPA/Hibernate,
- registro de usuarios,
- inicio de sesion por email y password,
- roles de cuenta,
- pantalla de login en React,
- visualizacion del tipo de cuenta en la interfaz.

### Tipos de cuenta actuales

El sistema ya contempla estos tipos:

- `ADMIN`: administracion general del sistema.
- `ORGANIZADOR`: publicacion de eventos, gestion de entradas y revision basica de ventas.
- `CLIENTE`: exploracion de eventos y compra o reserva de entradas.

Por ahora el rol se devuelve en la respuesta del login y el frontend lo muestra en la barra superior para poder probar el comportamiento de cada perfil.

### Como funciona la autenticacion en esta etapa

#### Backend

El backend expone:

- `POST /api/auth/register`
- `POST /api/auth/login`

En el registro:

1. se validan los datos del usuario,
2. se normaliza el email,
3. se verifica que no exista otro usuario con el mismo email,
4. la password se hashea con BCrypt,
5. se guarda el usuario en PostgreSQL.

En el login:

1. se busca el usuario por email,
2. se verifica que la cuenta este activa,
3. se compara la password enviada contra el hash guardado,
4. si coincide, se devuelve la informacion basica del usuario y su rol.

#### Frontend

El frontend tiene una pantalla de acceso en `frontend/src/pages/LoginPage.jsx`.

El flujo actual es:

1. el usuario completa email y password,
2. React llama a `POST /api/auth/login`,
3. si la respuesta es valida, guarda el usuario en `localStorage`,
4. la barra superior muestra nombre y tipo de cuenta,
5. el usuario puede cerrar sesion y limpiar ese estado local.

### Importante: que NO hace todavia esta autenticacion

Esta primera version no tiene todavia:

- JWT,
- refresh token,
- cookies seguras,
- control fino de permisos por endpoint,
- recuperacion de contrasena,
- sesiones persistidas en servidor.

O sea: hoy es una autenticacion academica inicial, suficiente para demostrar cuentas, login y roles, pero todavia no es un esquema de seguridad de produccion.

### Por que Java + Spring Boot facilita esto

Spring Boot simplifica este trabajo porque:

- organiza el backend por capas claras (`controller`, `service`, `repository`, `model`),
- integra JPA/Hibernate para mapear clases Java a tablas PostgreSQL,
- facilita validaciones con anotaciones como `@NotBlank`, `@Email` y `@Valid`,
- permite exponer endpoints REST de forma simple con `@RestController`,
- se integra bien con hashing de contrasenas mediante BCrypt,
- deja una base muy facil de extender luego a Spring Security, JWT y permisos reales.

En este proyecto concreto, eso permite empezar simple y crecer por etapas sin rehacer toda la arquitectura.

### Como probar el flujo

1. levantar el backend con PostgreSQL configurado,
2. registrar un usuario con `POST /api/auth/register`,
3. levantar el frontend React,
4. entrar a `/login`,
5. iniciar sesion con ese usuario,
6. verificar que la barra superior muestre el tipo de cuenta.

### Comandos recomendados

#### Backend

```powershell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.jvmArguments=-Duser.timezone=UTC'
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

### Siguiente etapa recomendada

Despues de cuentas, el siguiente modulo correcto es `eventos`, porque ahi se conecta el rol `ORGANIZADOR` con una funcionalidad concreta del sistema:

- crear eventos,
- listar eventos,
- publicarlos,
- relacionarlos con el usuario organizador.

## 17. Registro de avance: actualizacion de stack frontend

Se actualizo la configuracion del frontend para dejar de depender exclusivamente de Node 20 y permitir un stack mas moderno.

### Cambios realizados

- se actualizo la validacion de entorno para aceptar Node `20.19+`, `22.12+` o superior,
- se actualizo `react` y `react-dom` a la linea `19.x`,
- se actualizo `vite` a la linea `8.x`,
- se actualizo `@vitejs/plugin-react` a la linea `5.x`,
- se agrego `engines.node` en `frontend/package.json` para documentar compatibilidad.

### Motivo del cambio

El frontend estaba bloqueado artificialmente a Node 20 por un script local, aunque la herramienta base ya puede trabajar con versiones modernas de Node.

Con esta actualizacion:

- se evita pelear con instalacion manual de Node 20,
- se permite usar entornos actuales,
- se reduce friccion para desarrollo local,
- se mantiene compatibilidad con Vite moderno.

### Que hacer despues de actualizar dependencias

Dentro de `frontend/`:

```bash
npm install
npm run dev
```

Si el lockfile viejo genera conflictos, conviene regenerarlo:

```bash
rm -rf node_modules package-lock.json
npm install
```

## 18. Registro de avance: integracion frontend-backend para login

Se completo la primera conexion real entre el frontend React y el backend Spring Boot para el inicio de sesion.

### Cambios funcionales

- se agrego una pantalla de login en el frontend,
- se almacena la sesion actual en `localStorage`,
- la barra superior muestra nombre y tipo de cuenta,
- el backend habilita CORS para `http://localhost:5173` y `http://127.0.0.1:5173`,
- se corrigio el error `Failed to fetch` del navegador permitiendo llamadas desde Vite hacia Spring Boot.

### Archivos clave

- `frontend/src/pages/LoginPage.jsx`
- `frontend/src/services/authService.js`
- `frontend/src/utils/authStorage.js`
- `frontend/src/components/layout/Navbar.jsx`
- `src/main/java/com/demo/butaca/config/WebConfig.java`

### Estado actual de la prueba

El flujo validado hasta ahora es:

1. crear usuario desde Bruno o cualquier cliente HTTP usando `POST /api/auth/register`,
2. iniciar sesion con `POST /api/auth/login`,
3. abrir `http://localhost:5173/login`,
4. entrar con las mismas credenciales,
5. ver en la interfaz el nombre del usuario y su rol.

### Pendiente despues de este punto

- proteger endpoints por rol,
- agregar registro visual desde el frontend,
- crear el modulo de eventos para organizadores,
- conectar compras y reservas con cuentas reales.
