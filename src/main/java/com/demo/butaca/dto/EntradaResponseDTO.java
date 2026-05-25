package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntradaResponseDTO {
    @JsonProperty("id_entrada")
    private Long idEntrada;

    @JsonProperty("codigo_unico_entrada")
    private UUID codigoUnicoEntrada;

    @JsonProperty("estado_entrada")
    private String estadoEntrada;

    @JsonProperty("fecha_compra")
    private LocalDateTime fechaCompra;

    @JsonProperty("fecha_validacion")
    private LocalDateTime fechaValidacion;

    @JsonProperty("evento_id")
    private Long eventoId;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("compra_id")
    private Long compraId;
}
