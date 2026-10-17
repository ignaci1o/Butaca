package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

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

    @JsonProperty("precio_pagado")
    private BigDecimal precioPagado;

    @JsonProperty("fecha_emision")
    private LocalDateTime fechaEmision;

    @JsonProperty("fecha_validacion")
    private LocalDateTime fechaValidacion;

    @JsonProperty("evento_id")
    private Long eventoId;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("compra_id")
    private Long compraId;

    @JsonProperty("dni_asistente")
    private String dniAsistente;

    @JsonProperty("nombre_asistente")
    private String nombreAsistente;
}
