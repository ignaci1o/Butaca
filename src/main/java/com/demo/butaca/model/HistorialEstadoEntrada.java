package com.demo.butaca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_entrada")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialEstadoEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial_entrada")
    @EqualsAndHashCode.Include
    private Long idHistorialEntrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrada", nullable = false)
    private Entrada entrada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior_entrada", length = 20)
    private EstadoEntrada estadoAnteriorEntrada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo_entrada", nullable = false, length = 20)
    private EstadoEntrada estadoNuevoEntrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_responsable")
    private Usuario usuarioResponsable;

    @Column(name = "motivo_cambio_entrada", length = 300)
    private String motivoCambioEntrada;

    @Column(name = "fecha_cambio_entrada", nullable = false)
    private LocalDateTime fechaCambioEntrada;

    @PrePersist
    public void completarFecha() {
        if (fechaCambioEntrada == null) {
            fechaCambioEntrada = LocalDateTime.now();
        }
    }
}
