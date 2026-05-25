package com.demo.butaca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @Column(name = "titulo_evento", nullable = false, length = 200)
    private String tituloEvento;

    @Column(name = "descripcion_evento", columnDefinition = "TEXT")
    private String descripcionEvento;

    @Column(name = "ubicacion_evento", nullable = false, length = 300)
    private String ubicacionEvento;

    @Column(name = "detalles_ubicacion_evento", length = 500)
    private String detallesUbicacionEvento;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "hora_evento", nullable = false)
    private LocalTime horaEvento;

    @Column(name = "precio_entrada_evento", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioEntradaEvento;

    @Column(name = "cupos_totales_evento", nullable = false)
    private Integer cuposTotalesEvento;

    @Column(name = "cupos_disponibles_evento", nullable = false)
    private int cuposDisponibles;

    @Column(name = "estado_publicacion_evento", nullable = false, length = 20)
    private String estadoPublicacionEvento;

    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id_evento", nullable = false)
    private Usuario organizador;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrada> entradas = new ArrayList<>();

    public int getCuposDisponibleEvento() {
        return cuposDisponibles;
    }

    public void setCuposDisponibleEvento(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }
}
