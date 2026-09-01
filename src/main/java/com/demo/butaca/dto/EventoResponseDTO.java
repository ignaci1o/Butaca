package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponseDTO {
    @JsonProperty("id_evento")
    private Long idEvento;

    @JsonProperty("titulo_evento")
    private String tituloEvento;

    @JsonProperty("descripcion_evento")
    private String descripcionEvento;

    @JsonProperty("ubicacion_id")
    private Long ubicacionId;

    @JsonProperty("ubicacion_nombre")
    private String ubicacionNombre;

    @JsonProperty("ubicacion_direccion")
    private String ubicacionDireccion;

    @JsonProperty("capacidad_maxima")
    private Integer capacidadMaxima;

    @JsonProperty("fecha_evento")
    private LocalDate fechaEvento;

    @JsonProperty("hora_evento")
    private LocalTime horaEvento;

    @JsonProperty("precio_entrada_evento")
    private BigDecimal precioEntradaEvento;

    @JsonProperty("cupos_totales_evento")
    private Integer cuposTotalesEvento;

    @JsonProperty("cupos_disponibles")
    private Integer cuposDisponibles;

    @JsonProperty("estado_publicacion_evento")
    private String estadoPublicacionEvento;

    @JsonProperty("imagen_url")
    private String imagenUrl;

    @JsonProperty("categoria_id")
    private Long categoriaId;

    @JsonProperty("categoria_nombre")
    private String categoriaNombre;

    @JsonProperty("organizador_id_evento")
    private Long organizadorIdEvento;

    private Integer version;
}
