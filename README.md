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
