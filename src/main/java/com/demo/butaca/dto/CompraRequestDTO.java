package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompraRequestDTO {
    @NotNull
    @JsonProperty("evento_id")
    private Long eventoId;

    @NotNull
    @JsonProperty("usuario_id")
    private Long usuarioId;

    @Min(1)
    private Integer cantidad = 1;
}
