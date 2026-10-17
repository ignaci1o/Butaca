# Plan de Exposición de Butaca — 10 Minutos (Versión Final 13 Tablas)

Este documento propone una defensa del proyecto **desde el resultado hacia las decisiones**: primero se muestra que Butaca funciona de punta a punta y después se explica cómo se resolvieron todas las correcciones pedidas por la cátedra (Pasarela de Pago PCI-DSS, Verificación de Identidad KYC obligatoria, Método de Cruce de Información con firma SHA-256, Entrada Nominal por DNI y Trackeo en Bitácora de Seguridad) cumpliendo estrictamente **1FN, 2FN, 3FN y FNBC**.

---

## ⏱️ Distribución de los 10 Minutos

| Tiempo | Responsable | Núcleo | Qué se muestra o explica |
| --- | --- | --- | --- |
| 0:00–2:00 | Mariano | Resultado en vivo | Demo: filtro por Categorías, KYC biométrico RENAPER, pago PCI-DSS y emisión de entradas nominales con QR + DNI. |
| 2:00–3:30 | Santiago | Problema y Roles | Separación de vistas: Cliente, Organizador (escáner QR + DNI en puerta) y Administrador (gestión de roles y auditoría). |
| 3:30–5:30 | Ignacio | Diagrama ER (13 Tablas) y 3FN | Cómo se mueven los datos en el diagrama durante una compra y por qué cumple 1FN, 2FN y 3FN sin redundancia. |
| 5:30–7:30 | Leandro | Seguridad Informática | PCI-DSS en `pago`, KYC en `verificacion_identidad`, firma `SHA-256` en `comprobante`, `@Version` y bitácora `auditoria_seguridad`. |
| 7:30–9:15 | Mariano | Pruebas y Concurrencia | Los 4 tests de integración automatizados (`ButacaApplicationTests`) que validan todo el circuito. |
| 9:15–10:00 | Santiago | Cierre | Síntesis del valor académico y evolución a producción. |

---

## 1. Recorrido de Demo en Vivo (`http://localhost:8080`)

### Paso A — Vista Cliente (`cliente@butaca.com` / `cliente123`)
1. Mostrar el modo oscuro renovado con imágenes únicas por espectáculo y filtrar desde el botón **Categorías** de la barra superior.
2. Entrar a un evento y hacer clic en **Comprar entradas**.
3. En el Checkout:
   - Mostrar el **Paso 1 (Identidad verificada RENAPER)**: explicar que el backend bloquea cualquier compra si el usuario no tiene registro `VERIFICADA` en `verificacion_identidad`.
   - Mostrar el **Paso 2 (Titularidad Nominal)**: si se compran 2 entradas, se pide nombre y DNI de cada asistente individual.
   - Mostrar el **Paso 3 (Pasarela de Pago PCI-DSS)**: tarjeta interactiva o billetera virtual; aclarar que al servidor solo viajan los últimos 4 dígitos.
4. Al confirmar, mostrar la pantalla limpia del cliente con sus tickets QR nominales listos para presentar en puerta (sin abrumarlo con hashes técnicos).

### Paso B — Vista Organizador (`org@butaca.com` / `org123`)
1. En el panel del organizador, ir al **Control de Acceso en Puerta (QR + DNI Nominal)**.
2. Pegar el código UUID de la entrada recién comprada e ingresar primero un **DNI falso**: mostrar cómo el sistema **rechaza el ingreso** porque la entrada es nominal.
3. Ingresar el **DNI real del titular**: mostrar cómo el ticket pasa a `USADA` y queda registrado en `historial_estado_entrada` y `auditoria_seguridad`.

### Paso C — Vista Administrador (`admin@butaca.com` / `admin123`)
1. Mostrar la sección de **Usuarios y Roles**: cómo las cuentas nuevas nacen siempre como `CLIENTE` y solo el administrador puede promoverlas a `ORGANIZADOR`.
2. Mostrar la tabla de **Cruce Técnico de Compras y Pagos**: donde el administrador audita la referencia externa de pasarela, los últimos 4 dígitos PCI-DSS y el hash de integridad `SHA-256` del comprobante.
3. Mostrar la **Bitácora de Auditoría de Seguridad** en vivo.

---

## 2. Preguntas Clave del Profesor y Respuestas Exactas

### ¿Cómo verifican la identidad de alguien en la app antes de comprar?
Mediante la tabla **`verificacion_identidad`** (relación `1:1` con `usuario`). Antes de ejecutar `CompraService.comprarEntrada()`, el servicio verifica que el comprador posea `estado_verificacion = VERIFICADA`. Siguiendo el principio de minimización de datos (Ley 25.326), no guardamos imágenes biométricas crudas en la base, sino el método (`BIOMETRIA_RENAPER`), fechas y el token único `codigo_validacion_kyc`.

### ¿Cómo agregaron la pasarela de pago cumpliendo 3FN y seguridad?
Agregamos la entidad **`pago`** vinculada `1:1` con `compra`.
- **Seguridad (PCI-DSS):** Nunca se almacena el número completo de tarjeta (PAN) ni el CVV; solo se guarda `ultimos_cuatro_digitos`, `pasarela_pago`, `medio_pago` y la `referencia_externa_pago`.
- **3FN:** `pago` no repite `monto_total` ni `id_cliente`, porque ambos se obtienen sin redundancia a través de `compra` y la suma de `entrada.precio_pagado_entrada`.

### ¿Qué hace exactamente el método que cruza la información al comprar?
Es el método transaccional `CompraService.comprarEntrada()`, anotado con `@Transactional`. En una sola unidad atómica cruza **8 tablas**:
1. Valida identidad `VERIFICADA` en `verificacion_identidad`.
2. Bloquea optimistamente el evento con `@Version` en `evento` y verifica el cupo libre contando `entrada`.
3. Crea la cabecera en `compra`.
4. Registra la transacción aprobada en `pago`.
5. Emite cada ticket nominal con `dni_asistente_entrada` y `nombre_asistente_entrada` en `entrada` + su traza inicial en `historial_estado_entrada`.
6. Genera el `comprobante` calculando un hash `SHA-256` (`hash_cruce_seguridad`) que sella comprador + DNI + evento + pago + monto, y envía el aviso en `notificacion`.
7. Deja constancia inmutable en `auditoria_seguridad`.

### ¿Por qué hicieron la entrada nominal en la tabla `entrada` y no en `compra`?
Porque un mismo comprador puede adquirir 3 entradas en una sola compra (para él y dos acompañantes). Si el DNI estuviera en `compra`, habría grupos repetitivos (violando **1FN**) o las 3 personas tendrían el mismo DNI en su entrada. Al colocar `nombre_asistente_entrada` y `dni_asistente_entrada` en cada fila de **`entrada`**, cada ticket QR pertenece a un asistente físico único y se valida en puerta contra su DNI real.
