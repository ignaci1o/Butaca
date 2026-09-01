package com.demo.butaca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entrada")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrada extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    @EqualsAndHashCode.Include
    private Long idEntrada;

    @Column(name = "codigo_unico_entrada", nullable = false, unique = true)
    private UUID codigoUnicoEntrada;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_entrada", nullable = false, length = 20)
    private EstadoEntrada estadoEntrada = EstadoEntrada.CONFIRMADA;

    @Column(name = "precio_pagado_entrada", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPagado;

    @Column(name = "fecha_validacion_entrada")
    private LocalDateTime fechaValidacion;

    @Column(name = "fecha_emision_entrada", nullable = false)
    private LocalDateTime fechaEmision;

    @Version
    @Builder.Default
    @Column(name = "version_entrada", nullable = false)
    private Integer versionEntrada = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compra")
    private Compra compra;

    @PrePersist
    public void prePersistEntrada() {
        if (this.codigoUnicoEntrada == null) {
            this.codigoUnicoEntrada = UUID.randomUUID();
        }
        if (this.fechaEmision == null) {
            this.fechaEmision = LocalDateTime.now();
        }
    }
}
