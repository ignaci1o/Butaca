# Directrices Para Trabajar En Butaca

Este repositorio debe mantenerse alineado con `srs_proyecto.md`. Antes de cambiar arquitectura, contratos REST, modelo de datos o flujos de UI, leer ese archivo y este documento.

## Objetivo Técnico

Butaca es una plataforma academica de gestion y venta/reserva de entradas para eventos. El backend Spring Boot define el dominio relacional y el frontend React consume la API REST bajo `/api`.

## Reglas Del Dominio

- Roles validos: `ADMIN`, `ORGANIZADOR`, `CLIENTE`.
- Estados de evento: `BORRADOR`, `PUBLICADO`, `CANCELADO`.
- Estados de entrada: `CONFIRMADA`, `USADA`, `ANULADA`.
- Los clientes solo deben ver eventos `PUBLICADO` con cupos disponibles.
- Un evento pertenece a un organizador y a una ubicacion.
- Una entrada pertenece a un evento, a un cliente y opcionalmente a una compra.
- Las compras congelan el precio historico en cada entrada mediante `precio_pagado`.
- El control de concurrencia de eventos se apoya en `Evento.version` con `@Version`.

## Modelo De Datos

El modelo normalizado vigente usa estas entidades:

- `roles`: catalogo de roles.
- `usuarios`: credenciales, datos personales, estado de cuenta y rol.
- `ubicaciones`: lugares fisicos con direccion, capacidad maxima y estado activo.
- `categorias`: clasificacion de eventos.
- `eventos`: publicacion, cupos, precio, categoria, ubicacion y organizador.
- `compras`: cabecera transaccional de la operacion.
- `entradas`: tickets emitidos con UUID unico y precio historico.
- `notificaciones`: mensajes internos del MVP.

No volver a guardar datos de ubicacion como texto duplicado en `eventos` si ya existe `ubicacion_id`. Si se agregan campos derivados, documentar por que no introducen una anomalia de actualizacion.

## Backend

- Mantener controladores delgados; la regla de negocio vive en `service`.
- Mantener DTOs con `@JsonProperty` en snake_case para compatibilidad con la API actual.
- Evitar strings magicos para estados; usar enums del paquete `model`.
- No crear endpoints nuevos sin documentarlos en `README.md`.
- No duplicar handlers globales de excepciones. Usar `ApiExceptionHandler`.
- No exponer passwords ni hashes en DTOs de respuesta.

Comandos esperados:

```bash
./mvnw compile
./mvnw test
./mvnw spring-boot:run
./scripts/demo.sh
```

Requisito local: Java 17 instalado y `JAVA_HOME` configurado.

## Frontend

- Consumir la API desde `frontend/src/services/`.
- No leer usuarios desde claves sueltas de `localStorage`; usar `butaca-auth-user`.
- Normalizar la respuesta de auth en `authService.normalizeAuthUser`.
- Mantener `VITE_API_BASE_URL` configurable y con fallback a `http://localhost:8080/api`.
- Usar componentes y estilos existentes antes de introducir librerias nuevas.
- Usar `pnpm` como unico gestor de paquetes del frontend.
- Mantener `frontend/pnpm-lock.yaml` como lockfile versionable.
- No recrear ni versionar `package-lock.json`, `yarn.lock` ni locks de otros gestores.
- Si Vite falla por bindings nativos de Rolldown en WSL, ejecutar `pnpm install` dentro de `frontend/`.

Comandos esperados:

```bash
cd frontend
pnpm install
pnpm run dev
pnpm run build
```

## Dependencias

- Backend: Java 17, Spring Boot, Spring Web MVC, Spring Data JPA, Validation, PostgreSQL, BCrypt, Lombok.
- Demo local: perfil `demo` con H2 en memoria y datos semilla de `DataLoader`.
- Frontend: React, Vite, React Router DOM, gestionados con `pnpm`.
- No agregar dependencias para resolver problemas que pueden resolverse con APIs nativas o codigo existente.

## Editor Y Archivos Locales

- Usar Visual Studio Code como editor del proyecto.
- Mantener solo `.vscode/settings.json` y `.vscode/extensions.json` como configuracion versionable de editor.
- No versionar ni recrear carpetas de otros editores o agentes: `.idea/`, `.opencode/`, `.agents/`, `.codex/`.
- No versionar salidas generadas: `frontend/dist/`, `target/`, `node_modules/`.
- Si se necesita limpiar el workspace, eliminar artefactos generados y luego reinstalar dependencias con `pnpm install`.

## Documentacion

Al cambiar contratos, actualizar:

- `README.md`
- `srs_proyecto.md` si cambia un requisito
- este `AGENTS.MD` si cambia una regla de trabajo

## Verificacion Minima Antes De Entregar

- Backend compila con `./mvnw compile`.
- Frontend compila con `pnpm run build`.
- Demo integral levanta con `./scripts/demo.sh` cuando Java 17, pnpm y dependencias Maven ya estan disponibles.
- Login, registro, listado de eventos, compra, panel organizador y panel cliente siguen consumiendo endpoints reales.
- No commitear secretos, `.env`, certificados, `node_modules`, `target` ni `frontend/dist`.
