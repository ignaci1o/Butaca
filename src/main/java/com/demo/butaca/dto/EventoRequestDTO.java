package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventoRequestDTO {
    @NotBlank
    @JsonProperty("titulo_evento")
    private String tituloEvento;

    @JsonProperty("descripcion_evento")
    private String descripcionEvento;

    @NotBlank
    @JsonProperty("ubicacion_evento")
    private String ubicacionEvento;

    @JsonProperty("detalles_ubicacion_evento")
    private String detallesUbicacionEvento;

    @NotNull
    @JsonProperty("fecha_evento")
    private LocalDate fechaEvento;

    @NotNull
    @JsonProperty("hora_evento")
    private LocalTime horaEvento;

    @NotNull
    @JsonProperty("precio_entrada_evento")
    private BigDecimal precioEntradaEvento;

    @NotNull
    @JsonProperty("cupos_totales_evento")
    private Integer cuposTotalesEvento;

    @JsonProperty("estado_publicacion_evento")
    private String estadoPublicacionEvento;

    @JsonProperty("imagen_url")
    private String imagenUrl;

    @JsonProperty("categoria_id")
    private Long categoriaId;

    @NotNull
    @JsonProperty("organizador_id_evento")
    private Long organizadorIdEvento;
}
