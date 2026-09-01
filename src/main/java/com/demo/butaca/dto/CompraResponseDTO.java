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

    @JsonProperty("cliente_id")
    private Long clienteId;

    @JsonProperty("cliente_email")
    private String clienteEmail;

    @JsonProperty("evento_id")
    private Long eventoId;

    @JsonProperty("evento_titulo")
    private String eventoTitulo;

    @JsonProperty("estado_compra")
    private String estadoCompra;

    private List<EntradaResponseDTO> entradas;
}
