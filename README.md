# Butaca — Plataforma de Gestión de Eventos y Venta de Entradas Nominales

Sistema web full-stack (**Spring Boot + React + JPA**) para la gestión de espectáculos, verificación de identidad obligatoria (**KYC**), pasarela de pagos bajo normativa **PCI-DSS**, emisión de **entradas nominales con QR + DNI**, cruce transaccional con firma de integridad **SHA-256** y bitácora de **auditoría de seguridad**.

---

## 📚 Documentación y Diagramas del Proyecto

- 📊 **Diagrama Entidad-Relación Editable (Draw.io):** [`diagrama Butaca.drawio`](diagrama%20Butaca.drawio) / [`diagrama Butaca.drawio.xml`](diagrama%20Butaca.drawio.xml)
- 🗂️ **Modelo DBML (dbdiagram.io):** [`butaca.dbml`](butaca.dbml)
- 📐 **Justificación de 1FN, 2FN, 3FN y Seguridad:** [`docs/normalizacion-base-datos.md`](docs/normalizacion-base-datos.md)
- 📖 **Glosario de las 13 Tablas y Preguntas de Defensa:** [`docs/glosario-y-modelo-base-de-datos.md`](docs/glosario-y-modelo-base-de-datos.md)
- 🎤 **Plan de Exposición de 10 Minutos:** [`docs/plan-exposicion-10-minutos.md`](docs/plan-exposicion-10-minutos.md)
- 🧭 **Guía Rápida de Presentación y Consultas SQL H2:** [`docs/guia-presentacion.md`](docs/guia-presentacion.md)
- 📋 **Especificación de Requisitos de Software (SRS):** [`srs_proyecto.md`](srs_proyecto.md)

---

## ✅ Correcciones del Profesor Implementadas

1. **Verificación de Identidad Obligatoria antes de Comprar (`verificacion_identidad`):**
   - Ningún usuario puede adquirir entradas sin haber validado previamente su identidad (**KYC** mediante validación biométrica/documental simulada con RENAPER).
   - Cumple con la **Ley 25.326 de Protección de Datos Personales**: no almacena fotos crudas del rostro ni del DNI, sino el estado (`VERIFICADA`), el método empleado y el token de validación (`codigo_validacion_kyc`).
2. **Pasarela de Pago Integrada al Modelo bajo PCI-DSS (`pago`):**
   - Modela el procesamiento con pasarelas (`MERCADOPAGO`, `STRIPE`, `MODO`) y medios de pago (`TARJETA_CREDITO`, `TARJETA_DEBITO`, `BILLETERA_VIRTUAL`).
   - Cumple estrictamente **PCI-DSS**: jamás persiste los 16 dígitos de la tarjeta (PAN) ni el código CVV; únicamente almacena los `ultimos_cuatro_digitos` y el token `referencia_externa_pago`.
3. **Método Transaccional de Cruce de Información (`CompraService.comprarEntrada`):**
   - Operación atómica (`@Transactional`) que cruza en tiempo real:
     1. Identidad `VERIFICADA` del comprador (`verificacion_identidad`).
     2. Disponibilidad real del espectáculo y control de concurrencia con bloqueo optimista `@Version` (`evento`).
     3. Aprobación del cobro en la pasarela (`pago`).
     4. Emisión de tickets nominales individuales (`entrada` + `historial_estado_entrada`).
     5. Generación del comprobante electrónico con **firma criptográfica SHA-256** (`comprobante`) y envío de confirmación (`notificacion`).
     6. Registro de auditoría en la bitácora de seguridad (`auditoria_seguridad`).
4. **Entrada Nominal Óptima (`entrada`):**
   - Cada ticket individual emitido posee `codigo_unico_entrada` (QR), `nombre_asistente_entrada` y `dni_asistente_entrada`.
   - Permite que un comprador verificado compre entradas para sí mismo y acompañantes asignando el nombre y DNI real de cada asistente, erradicando la reventa y habilitando el **control en puerta por QR + DNI físico**.
5. **Trackeo de Datos e Inmutabilidad (`auditoria_seguridad` + `historial_estado_entrada`):**
   - Bitácora doble *append-only*: `historial_estado_entrada` audita cada cambio de estado de cada ticket (`CONFIRMADA`, `USADA`, `ANULADA`), mientras que `auditoria_seguridad` registra logins, verificaciones KYC, compras aprobadas/bloqueadas, cambios de rol y lecturas en puerta con fecha/hora e IP.
6. **Separación de Vistas y Permisos por Rol (Principio de Mínimo Privilegio):**
   - **Registro público:** Solo permite crear cuentas con rol `CLIENTE` (e incluye la solicitud *"Sumá tu organización"* en el pie de página para revisión del administrador).
   - **Vista Cliente:** Muestra un checkout limpio (validación biométrica RENAPER + tarjeta interactiva) y entradas listas para presentar en puerta, ocultando hashes técnicos internos.
   - **Vista Organizador:** Incluye validador de acceso en puerta (**Escáner QR + verificación de DNI nominal**) y gestión de eventos propios.
   - **Vista Administrador:** Permite gestionar usuarios, promover cuentas a `ORGANIZADOR` y auditar la tabla de cruce técnico de compras (`SHA-256`, tokens de pasarela y bitácora de seguridad).

---

## 🗄️ Diagrama Entidad-Relación Completo (13 Tablas en 3FN)

```mermaid
erDiagram
    ROL ||--o{ USUARIO : "asigna (1:N)"
    USUARIO ||--o| VERIFICACION_IDENTIDAD : "valida KYC (1:1)"
    USUARIO ||--o{ AUDITORIA_SEGURIDAD : "es auditado en (1:N)"
    USUARIO ||--o{ EVENTO : "organiza (1:N)"
    UBICACION ||--o{ EVENTO : "aloja (1:N)"
    CATEGORIA ||--o{ EVENTO : "clasifica (1:N)"
    USUARIO ||--o{ COMPRA : "realiza (1:N)"
    COMPRA ||--|| PAGO : "procesa en pasarela (1:1)"
    COMPRA ||--|| COMPROBANTE : "respalda con firma (1:1)"
    EVENTO ||--o{ ENTRADA : "emite tickets (1:N)"
    COMPRA ||--o{ ENTRADA : "agrupa tickets nominales (1:N)"
    ENTRADA ||--o{ HISTORIAL_ESTADO_ENTRADA : "audita estados (1:N)"
    USUARIO ||--o{ NOTIFICACION : "recibe avisos (1:N)"
    EVENTO ||--o{ NOTIFICACION : "origina avisos (1:N)"

    ROL {
        bigint id_rol PK
        varchar nombre_rol UK "ADMIN | ORGANIZADOR | CLIENTE"
    }

    USUARIO {
        bigint id_usuario PK
        varchar nombre_usuario
        varchar apellido_usuario
        varchar email_usuario UK
        varchar dni_usuario UK
        varchar password_usuario "Hash BCrypt"
        varchar telefono_usuario
        varchar estado_usuario "ACTIVO | INACTIVO | BLOQUEADO"
        bigint id_rol FK
    }

    VERIFICACION_IDENTIDAD {
        bigint id_verificacion PK
        bigint id_usuario FK, UK "Relacion 1:1 con USUARIO"
        varchar metodo_verificacion "BIOMETRIA_RENAPER | DOCUMENTO_DNI"
        varchar estado_verificacion "PENDIENTE | VERIFICADA | RECHAZADA"
        varchar codigo_validacion_kyc UK
        varchar observacion_verificacion
        timestamp fecha_solicitud
        timestamp fecha_verificacion
    }

    UBICACION {
        bigint id_ubicacion PK
        varchar nombre_ubicacion UK
        varchar direccion_ubicacion
        int capacidad_maxima_ubicacion
        boolean activa_ubicacion
    }

    CATEGORIA {
        bigint id_categoria PK
        varchar nombre_categoria UK
        varchar descripcion_categoria
    }

    EVENTO {
        bigint id_evento PK
        varchar titulo_evento
        varchar descripcion_evento
        timestamp fecha_inicio_evento
        decimal precio_actual_evento
        int cupo_total_evento
        varchar estado_evento "BORRADOR | PUBLICADO | CANCELADO"
        varchar imagen_url_evento
        bigint version_evento "Bloqueo Optimista @Version"
        bigint id_organizador FK
        bigint id_categoria FK
        bigint id_ubicacion FK
    }

    COMPRA {
        bigint id_compra PK
        bigint id_cliente FK
        timestamp fecha_compra
        varchar estado_compra "PENDIENTE | CONFIRMADA | CANCELADA"
    }

    PAGO {
        bigint id_pago PK
        bigint id_compra FK, UK "Relacion 1:1 con COMPRA"
        varchar pasarela_pago "MERCADOPAGO | STRIPE | MODO"
        varchar medio_pago "TARJETA_CREDITO | DEBITO | BILLETERA"
        varchar estado_pago "APROBADO | RECHAZADO | REEMBOLSADO"
        varchar referencia_externa_pago UK "Token de Pasarela"
        varchar ultimos_cuatro_digitos "PCI-DSS (sin CVV ni PAN)"
        timestamp fecha_pago
    }

    COMPROBANTE {
        bigint id_comprobante PK
        bigint id_compra FK, UK "Relacion 1:1 con COMPRA"
        varchar numero_comprobante UK "Ej: CMP-20261017-A1B2C3"
        varchar tipo_comprobante "RECIBO_DIGITAL | FACTURA_B"
        varchar hash_cruce_seguridad UK "Firma SHA-256 del cruce"
        varchar estado_envio_entrada "ENVIADO | PENDIENTE | FALLIDO"
        timestamp fecha_emision_comprobante
    }

    ENTRADA {
        bigint id_entrada PK
        bigint id_evento FK
        bigint id_compra FK
        varchar codigo_unico_entrada UK "UUID unico para QR"
        varchar dni_asistente_entrada "Nominalidad: DNI"
        varchar nombre_asistente_entrada "Nominalidad: Titular"
        decimal precio_pagado_entrada "Precio historico congelado"
        varchar estado_entrada "CONFIRMADA | USADA | ANULADA"
        timestamp fecha_emision_entrada
    }

    HISTORIAL_ESTADO_ENTRADA {
        bigint id_historial_estado_entrada PK
        bigint id_entrada FK
        varchar estado_anterior_entrada
        varchar estado_nuevo_entrada
        varchar motivo_cambio_entrada
        timestamp fecha_cambio_entrada
    }

    NOTIFICACION {
        bigint id_notificacion PK
        bigint id_usuario_destino FK
        bigint id_evento_relacionado FK
        varchar tipo_notificacion
        varchar mensaje_notificacion
        boolean leida_notificacion
        timestamp fecha_creacion_notificacion
    }

    AUDITORIA_SEGURIDAD {
        bigint id_auditoria PK
        bigint id_usuario FK
        varchar accion_auditoria "REGISTRO | KYC | CRUCE_COMPRA | VALIDACION_PUERTA"
        varchar entidad_afectada
        varchar id_registro_afectado
        varchar resultado_operacion "EXITOSO | BLOQUEADO | RECHAZADO"
        varchar ip_origen
        varchar detalle_cruce
        timestamp fecha_evento_auditoria
    }
```

---

## 🔄 ¿Cómo interactúa la Página Web con el Diagrama paso a paso?

### 1. Registro de Usuario (`/registro`)
1. El usuario completa sus datos en la web.
2. `AuthService` consulta **`ROL`** para asignar obligatoriamente el rol `CLIENTE` (impidiendo escalamiento de privilegios desde el formulario público).
3. Se persiste el usuario en **`USUARIO`** con su contraseña cifrada mediante `BCrypt`.
4. Se graba el alta en **`AUDITORIA_SEGURIDAD`** (`accion_auditoria = REGISTRO_USUARIO`).

### 2. Navegación y Filtro por Categorías (`/` y `/buscar?categoria=...`)
1. Al abrir el catálogo o usar el menú desplegable **Categorías** del Navbar, el frontend consulta `GET /api/eventos`.
2. `EventoService` une **`EVENTO`** con **`CATEGORIA`** y **`UBICACION`** (obteniendo dirección y capacidad sin redundancia 3FN) y calcula el cupo disponible restando las filas activas (`CONFIRMADA` y `USADA`) en **`ENTRADA`**.

### 3. Verificación de Identidad Obligatoria KYC (`/checkout/:eventoId` — Paso 1)
1. Antes de habilitar el pago, el checkout consulta `GET /api/verificacion-identidad/usuario/{id}` sobre **`VERIFICACION_IDENTIDAD`**.
2. Si el usuario aún no está verificado, completa el asistente biométrico/documental (RENAPER).
3. `VerificacionIdentidadService` valida que el `dni_usuario` no pertenezca a otra cuenta en **`USUARIO`**, crea o actualiza la fila en **`VERIFICACION_IDENTIDAD`** con `estado_verificacion = VERIFICADA` y `codigo_validacion_kyc`, y registra la validación en **`AUDITORIA_SEGURIDAD`**.

### 4. Método de Cruce de Información al Comprar (`POST /api/compras` — Pasos 2 y 3)
Al presionar **"FINALIZAR LA COMPRA"**, `CompraService.comprarEntrada()` ejecuta una **única transacción ACID (`@Transactional`)** que recorre **8 tablas**:
1. **Cruce de Identidad:** Comprueba en **`USUARIO`** y **`VERIFICACION_IDENTIDAD`** que la cuenta esté `ACTIVO` y `VERIFICADA`. Si no cumple, aborta la compra y registra `CRUCE_COMPRA_BLOQUEADO` en **`AUDITORIA_SEGURIDAD`**.
2. **Cruce de Stock y Concurrencia:** Bloquea optimistamente la fila en **`EVENTO`** (`@Version`) y cuenta en **`ENTRADA`** que existan butacas disponibles.
3. **Cabecera Comercial:** Inserta la orden en **`COMPRA`** (`estado_compra = CONFIRMADA`).
4. **Pasarela de Pago (PCI-DSS):** Procesa el cobro y registra en **`PAGO`** la `pasarela_pago`, `medio_pago`, `referencia_externa_pago` y solo los `ultimos_cuatro_digitos` (`estado_pago = APROBADO`).
5. **Emisión de Entradas Nominales:** Por cada asistente, inserta una fila en **`ENTRADA`** con su `codigo_unico_entrada` (UUID), `nombre_asistente_entrada`, `dni_asistente_entrada` y `precio_pagado_entrada`, y crea su primer registro en **`HISTORIAL_ESTADO_ENTRADA`**.
6. **Comprobante con Firma SHA-256 y Envío:** Calcula el `hash_cruce_seguridad` (`SHA-256` de compra + cliente + DNI + evento + referencia de pago + monto + fecha), guarda el recibo en **`COMPROBANTE`** con `estado_envio_entrada = ENVIADO` y emite el aviso en **`NOTIFICACION`**.
7. **Trackeo de Seguridad:** Registra el cruce exitoso en **`AUDITORIA_SEGURIDAD`** (`CRUCE_COMPRA_EXITOSO`).

### 5. Validación en Puerta por el Organizador (`/organizador`)
1. El organizador ingresa el código QR del ticket y el DNI físico presentado por el asistente en la puerta.
2. `EntradaService.usarEntrada()` busca el ticket en **`ENTRADA`**, verifica que esté `CONFIRMADA` y **compara el `dni_asistente_entrada` nominal con el DNI presentado**.
3. Si coinciden, actualiza **`ENTRADA`** a `USADA`, inserta la transición en **`HISTORIAL_ESTADO_ENTRADA`** (`CONFIRMADA -> USADA`) y registra la lectura en **`AUDITORIA_SEGURIDAD`**. Si el DNI no coincide o el QR ya fue utilizado, rechaza el ingreso y audita la anomalía.

---

## 🛡️ Cumplimiento de Formas Normales y Seguridad Informática

| Criterio | Implementación en Butaca |
| --- | --- |
| **1FN (Atómico)** | Cada atributo contiene un único valor atómico. Una compra de $N$ entradas genera $N$ filas independientes en `entrada`, cada una con su propio titular, DNI y QR. |
| **2FN (Dependencia Funcional Completa)** | Todas las tablas poseen clave primaria simple (`BIGINT`) y sus atributos dependen íntegramente de dicha clave. |
| **3FN y FNBC (Sin Dependencias Transitivas)** | • `rol`, `categoria` y `ubicacion` están separados en tablas maestras.<br>• `compra` no duplica cantidad ni monto total (se calculan desde `entrada.precio_pagado_entrada`).<br>• `pago` y `comprobante` no duplican el monto ni el `id_cliente` (se obtienen por `JOIN` con `compra` y `entrada`).<br>• `evento` no persiste `cupos_disponibles` como columna redundante. |
| **PCI-DSS (Seguridad en Medios de Pago)** | Jamás se almacena el PAN completo (16 dígitos) ni el CVV. Solo se persisten `ultimos_cuatro_digitos` y el token `referencia_externa_pago`. |
| **Integridad Criptográfica** | Contraseñas hasheadas con `BCrypt` y comprobantes sellados con `SHA-256` (`hash_cruce_seguridad`). |
| **Control de Concurrencia** | Bloqueo optimista `@Version` en `Evento` (`OPTIMISTIC_FORCE_INCREMENT`) dentro de transacciones `@Transactional` para impedir sobreventa. |

---

## 🚀 Requisitos y Ejecución Rápida

### Requisitos
- **Java 17** (`JAVA_HOME` configurado)
- **Node.js >= 20** y **pnpm** (solo si se desea recompilar el frontend; el build estático ya está incluido en `src/main/resources/static`)

### Opción 1: Iniciar en Windows con doble clic (Modo Demo H2)
Ejecutar el archivo [`iniciar-windows.bat`](iniciar-windows.bat) o desde PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

Una vez iniciado, abrir en el navegador:
- **Aplicación Web Completa:** `http://localhost:8080`
- **API REST:** `http://localhost:8080/api`
- **Consola Base de Datos H2:** `http://localhost:8080/h2-console`
  - **JDBC URL:** `jdbc:h2:mem:butaca_demo`
  - **User Name:** `sa`
  - **Password:** *(dejar vacío)*

### Opción 2: Levantar Backend (8080) + Frontend con Hot-Reload (5173)
```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\demo.ps1
```

### 🔑 Credenciales de Prueba Precargadas (`DataLoader`)

| Rol | Email | Contraseña | Qué permite probar |
| --- | --- | --- | --- |
| **Cliente** | `cliente@butaca.com` | `cliente123` | Explorar catálogo, filtrar por categorías, verificar identidad KYC (RENAPER), comprar entradas nominales y ver mis tickets QR. |
| **Organizador** | `org@butaca.com` | `org123` | Crear/publicar/cancelar eventos y **validar acceso en puerta con QR + DNI nominal**. |
| **Administrador** | `admin@butaca.com` | `admin123` | Gestionar ubicaciones y categorías, **cambiar roles de usuarios (`CLIENTE` ↔ `ORGANIZADOR`)** y auditar la tabla técnica de cruce de compras (`SHA-256`, pasarela y bitácora). |

---

## 🔌 Endpoints Principales de la API REST

### Autenticación e Identidad (KYC)
- `POST /api/auth/register` — Registro público de cuentas `CLIENTE`.
- `POST /api/auth/login` — Inicio de sesión con validación `BCrypt`.
- `GET /api/verificacion-identidad/usuario/{usuarioId}` — Consulta estado KYC del usuario.
- `POST /api/verificacion-identidad/verificar` — Ejecuta la validación KYC obligatoria antes de comprar.

### Catálogo y Eventos
- `GET /api/eventos` — Lista eventos publicados con disponibilidad calculada.
- `GET /api/eventos/{id}` — Detalle de un espectáculo.
- `POST /api/eventos` / `PUT /api/eventos/{id}` — Creación y edición por organizador.
- `PUT /api/eventos/{id}/publicar` / `PUT /api/eventos/{id}/cancelar` — Ciclo de vida del evento.

### Compras, Pasarela de Pago y Entradas Nominales
- `POST /api/compras` — **Método transaccional de cruce de información** (KYC + Stock `@Version` + Pago PCI-DSS + Emisión Nominal + Comprobante `SHA-256` + Notificación + Auditoría).
- `GET /api/compras/{id}` / `GET /api/compras/usuario/{id}` / `GET /api/compras/organizador/{id}`
- `GET /api/entradas/validar/{codigo}` — Consulta estado y titularidad nominal de un QR en puerta.
- `PUT /api/entradas/{id}/usar?dniPresentado={dni}` — Consume la entrada en puerta validando coincidencia de DNI.

### Administración y Auditoría de Seguridad
- `GET /api/admin/dashboard` / `GET /api/admin/stats` — Métricas e historial de compras cruzadas.
- `GET /api/admin/usuarios` / `PUT /api/admin/usuarios/{id}/rol` — Gestión de cuentas y asignación del rol `ORGANIZADOR`.
- `GET /api/admin/auditoria` — Bitácora inmutable de seguridad (`auditoria_seguridad`).
- `GET /api/admin/categorias` / `POST /api/admin/categorias`
- `GET /api/admin/ubicaciones` / `POST /api/admin/ubicaciones`

---

## 🧪 Ejecución de Pruebas Automatizadas

```powershell
.\mvnw.cmd test
```

Las pruebas de integración en [`ButacaApplicationTests.java`](src/test/java/com/demo/butaca/ButacaApplicationTests.java) verifican automáticamente:
1. Filtrado del catálogo público por disponibilidad real.
2. Bloqueo de seguridad cuando un usuario intenta comprar sin verificación de identidad KYC.
3. Cruce transaccional completo de compra (emisión de entradas nominales con DNI por asistente, pago PCI-DSS, comprobante con hash `SHA-256`, incremento de `@Version` e historial).
4. Validación en puerta con DNI nominal y rechazo ante DNI incorrecto o segundo intento de uso.
