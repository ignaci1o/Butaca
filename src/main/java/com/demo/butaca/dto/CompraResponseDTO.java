package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {
    private Long id;

    @JsonProperty("fecha_compra")
    private LocalDateTime fechaCompra;

    private Integer cantidad;
    private BigDecimal total;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    @JsonProperty("usuario_email")
    private String usuarioEmail;

    @JsonProperty("evento_id")
    private Long eventoId;

    @JsonProperty("evento_titulo")
    private String eventoTitulo;

    private List<EntradaResponseDTO> entradas;
}
