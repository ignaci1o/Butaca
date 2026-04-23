package com.demo.butaca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @Column(name = "titulo_evento", nullable = false, length = 200)
    private String tituloEvento;

    @Column(name = "descripcion_evento", columnDefinition = "TEXT")
    private String descripcionEvento;

    @Column(name = "ubicacion_evento", nullable = false, length = 150)
    private String ubicacionEvento;

    @Column(name = "detalles_ubicacion_evento", length = 255)
    private String detallesUbicacionEvento;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "hora_evento", nullable = false)
    private LocalTime horaEvento;

    @Column(name = "precio_entrada_evento", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioEntradaEvento;

    @Column(name = "cupos_totales_evento", nullable = false)
    private Integer cuposTotalesEvento;

    @Column(name = "cupos_disponible_evento", nullable = false)
    private Integer cuposDisponibleEvento;

    @Column(name = "estado_publicacion_evento", nullable = false, length = 20)
    private String estadoPublicacionEvento; // enum: BORRADOR, PUBLICADO, CANCELADO

    @ManyToOne
    @JoinColumn(name = "organizador_id_evento", nullable = false)
    private Usuario organizador;

    public Evento() {
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public String getUbicacionEvento() {
        return ubicacionEvento;
    }

    public void setUbicacionEvento(String ubicacionEvento) {
        this.ubicacionEvento = ubicacionEvento;
    }

    public String getDetallesUbicacionEvento() {
        return detallesUbicacionEvento;
    }

    public void setDetallesUbicacionEvento(String detallesUbicacionEvento) {
        this.detallesUbicacionEvento = detallesUbicacionEvento;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public LocalTime getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(LocalTime horaEvento) {
        this.horaEvento = horaEvento;
    }

    public BigDecimal getPrecioEntradaEvento() {
        return precioEntradaEvento;
    }

    public void setPrecioEntradaEvento(BigDecimal precioEntradaEvento) {
        this.precioEntradaEvento = precioEntradaEvento;
    }

    public Integer getCuposTotalesEvento() {
        return cuposTotalesEvento;
    }

    public void setCuposTotalesEvento(Integer cuposTotalesEvento) {
        this.cuposTotalesEvento = cuposTotalesEvento;
    }

    public Integer getCuposDisponibleEvento() {
        return cuposDisponibleEvento;
    }

    public void setCuposDisponibleEvento(Integer cuposDisponibleEvento) {
        this.cuposDisponibleEvento = cuposDisponibleEvento;
    }

    public String getEstadoPublicacionEvento() {
        return estadoPublicacionEvento;
    }

    public void setEstadoPublicacionEvento(String estadoPublicacionEvento) {
        this.estadoPublicacionEvento = estadoPublicacionEvento;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }
}
