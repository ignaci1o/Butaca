package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    @JsonProperty("id_categoria")
    private Long idCategoria;

    @NotBlank
    private String nombre;

    private String descripcion;
}
