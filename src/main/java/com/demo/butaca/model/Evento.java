package com.demo.butaca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    @EqualsAndHashCode.Include
    private Long idEvento;

    @Column(name = "titulo_evento", nullable = false, length = 200)
    private String tituloEvento;

    @Column(name = "descripcion_evento", columnDefinition = "TEXT")
    private String descripcionEvento;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "hora_evento", nullable = false)
    private LocalTime horaEvento;

    @Column(name = "precio_actual_entrada", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioEntradaEvento;

    @Column(name = "cupo_total_evento", nullable = false)
    private Integer cuposTotalesEvento;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_evento", nullable = false, length = 20)
    private EstadoEvento estadoPublicacionEvento = EstadoEvento.BORRADOR;

    @Version
    @Builder.Default
    @Column(name = "version_evento", nullable = false)
    private Integer version = 0;

    @Column(name = "imagen_url_evento", columnDefinition = "TEXT")
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizador", nullable = false)
    private Usuario organizador;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrada> entradas = new ArrayList<>();
}
