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
public class ComprobanteResponseDTO {
    @JsonProperty("id_comprobante")
    private Long idComprobante;

    @JsonProperty("pago_id")
    private Long pagoId;

    @JsonProperty("numero_comprobante")
    private String numeroComprobante;

    @JsonProperty("tipo_comprobante")
    private String tipoComprobante;

    @JsonProperty("hash_cruce_seguridad")
    private String hashCruceSeguridad;

    @JsonProperty("estado_envio_comprobante")
    private String estadoEnvioComprobante;

    @JsonProperty("fecha_emision_comprobante")
    private LocalDateTime fechaEmisionComprobante;
}
