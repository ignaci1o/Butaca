package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    @JsonProperty("id_ubicacion")
    private Long idUbicacion;

    @NotBlank
    @JsonProperty("nombre_lugar")
    private String nombreLugar;

    @NotBlank
    private String direccion;

    @NotNull
    @Min(1)
    @JsonProperty("capacidad_maxima")
    private Integer capacidadMaxima;

    private Boolean activo;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
