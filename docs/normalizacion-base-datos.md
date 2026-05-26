# Verificacion De Normalizacion - Butaca

Este documento compara el SRS (`srs_proyecto.md`) y el modelo tecnico del proyecto contra el modelo JPA actual.

## Modelo Normalizado Vigente

```text
roles 1--N usuarios
usuarios 1--N eventos           (organizador)
ubicaciones 1--N eventos
categorias 1--N eventos
eventos 1--N compras
usuarios 1--N compras           (cliente)
compras 1--N entradas
eventos 1--N entradas
usuarios 1--N entradas          (cliente)
usuarios 1--N notificaciones
eventos 0..1--N notificaciones
```

## 1FN

Cumple. Todas las tablas tienen columnas atomicas:

- No hay listas de entradas en `eventos`.
- No hay multiples telefonos, roles o ubicaciones en una misma celda.
- Cada entrada se representa como una fila independiente con UUID propio.

## 2FN

Cumple. Todas las entidades usan clave primaria simple y los atributos dependen de esa clave:

- Los datos personales dependen de `usuarios.id_usuario`.
- Los datos de lugar dependen de `ubicaciones.id_ubicacion`.
- El precio historico pagado depende de `entradas.id_entrada`.

## 3FN

Cumple para el alcance actual:

- `roles`, `categorias` y `ubicaciones` evitan repetir valores descriptivos en `usuarios` y `eventos`.
- `entradas.precio_pagado` congela un dato historico de la transaccion; no duplica el precio actual del evento.
- `compras.total` representa el total de la operacion registrada y permite auditoria de la transaccion.

## BCNF Practica

El modelo queda en una forma equivalente a BCNF para las dependencias principales:

- `usuarios.email_usuario` es unico.
- `roles.nombre` es unico.
- `categorias.nombre` es unico.
- `entradas.codigo_unico_entrada` es unico.
- `ubicaciones(nombre_lugar, direccion)` es unico.

## Decisiones Deliberadas

`eventos.cupos_disponibles_evento` es un contador operacional. Puede derivarse desde entradas emitidas, pero se mantiene por rendimiento y para cumplir RF4. La anomalia de actualizacion se controla desde `EventoService.descontarCupos` dentro de transaccion y con `@Version`.

`compras` se mantiene como cabecera transaccional aunque el SRS minimo podria funcionar solo con `entradas`. Esto permite representar una compra de multiples entradas sin repetir fecha, cliente y total de operacion en cada ticket.

## Brechas Cerradas

- Se agrego `ubicaciones`.
- Se agrego auditoria uniforme (`created_at`, `updated_at`).
- Se agrego `eventos.version` para bloqueo optimista.
- Se normalizaron roles como tabla separada.
- Se agrego `entradas.precio_pagado`.
- Se reemplazaron strings de estado por enums.
- Se elimino el handler duplicado de excepciones.
