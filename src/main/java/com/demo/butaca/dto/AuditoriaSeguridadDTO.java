package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaSeguridadDTO {
    @JsonProperty("id_auditoria")
    private Long idAuditoria;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("email_usuario")
    private String emailUsuario;

    @JsonProperty("accion_auditoria")
    private String accionAuditoria;

    @JsonProperty("entidad_afectada")
    private String entidadAfectada;

    @JsonProperty("id_registro_afectado")
    private Long idRegistroAfectado;

    @JsonProperty("resultado_auditoria")
    private String resultadoAuditoria;

    @JsonProperty("ip_origen")
    private String ipOrigen;

    @JsonProperty("detalle_tracking")
    private String detalleTracking;

    @JsonProperty("fecha_auditoria")
    private LocalDateTime fechaAuditoria;
}
