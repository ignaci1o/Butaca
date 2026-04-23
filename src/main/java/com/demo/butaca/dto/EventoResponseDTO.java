package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventoResponseDTO {

    @JsonProperty("id_evento")
    private Long idEvento;

    @JsonProperty("titulo_evento")
    private String tituloEvento;

    @JsonProperty("descripcion_evento")
    private String descripcionEvento;

    @JsonProperty("ubicacion_evento")
    private String ubicacionEvento;

    @JsonProperty("detalles_ubicacion_evento")
    private String detallesUbicacionEvento;

    @JsonProperty("fecha_evento")
    private LocalDate fechaEvento;

    @JsonProperty("hora_evento")
    private LocalTime horaEvento;

    @JsonProperty("precio_entrada_evento")
    private BigDecimal precioEntradaEvento;

    @JsonProperty("cupos_totales_evento")
    private Integer cuposTotalesEvento;

    @JsonProperty("cupos_disponible_evento")
    private Integer cuposDisponibleEvento;

    @JsonProperty("estado_publicacion_evento")
    private String estadoPublicacionEvento;

    @JsonProperty("organizador_id_evento")
    private Long organizadorIdEvento;

    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
    public String getTituloEvento() { return tituloEvento; }
    public void setTituloEvento(String tituloEvento) { this.tituloEvento = tituloEvento; }
    public String getDescripcionEvento() { return descripcionEvento; }
    public void setDescripcionEvento(String descripcionEvento) { this.descripcionEvento = descripcionEvento; }
    public String getUbicacionEvento() { return ubicacionEvento; }
    public void setUbicacionEvento(String ubicacionEvento) { this.ubicacionEvento = ubicacionEvento; }
    public String getDetallesUbicacionEvento() { return detallesUbicacionEvento; }
    public void setDetallesUbicacionEvento(String detallesUbicacionEvento) { this.detallesUbicacionEvento = detallesUbicacionEvento; }
    public LocalDate getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDate fechaEvento) { this.fechaEvento = fechaEvento; }
    public LocalTime getHoraEvento() { return horaEvento; }
    public void setHoraEvento(LocalTime horaEvento) { this.horaEvento = horaEvento; }
    public BigDecimal getPrecioEntradaEvento() { return precioEntradaEvento; }
    public void setPrecioEntradaEvento(BigDecimal precioEntradaEvento) { this.precioEntradaEvento = precioEntradaEvento; }
    public Integer getCuposTotalesEvento() { return cuposTotalesEvento; }
    public void setCuposTotalesEvento(Integer cuposTotalesEvento) { this.cuposTotalesEvento = cuposTotalesEvento; }
    public Integer getCuposDisponibleEvento() { return cuposDisponibleEvento; }
    public void setCuposDisponibleEvento(Integer cuposDisponibleEvento) { this.cuposDisponibleEvento = cuposDisponibleEvento; }
    public String getEstadoPublicacionEvento() { return estadoPublicacionEvento; }
    public void setEstadoPublicacionEvento(String estadoPublicacionEvento) { this.estadoPublicacionEvento = estadoPublicacionEvento; }
    public Long getOrganizadorIdEvento() { return organizadorIdEvento; }
    public void setOrganizadorIdEvento(Long organizadorIdEvento) { this.organizadorIdEvento = organizadorIdEvento; }
}
