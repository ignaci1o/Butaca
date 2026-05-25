package com.demo.butaca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entradas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long idEntrada;

    @Column(name = "codigo_unico_entrada", nullable = false, unique = true)
    private UUID codigoUnicoEntrada;

    @Column(name = "estado_entrada", nullable = false, length = 20)
    private String estadoEntrada;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @PrePersist
    public void generarCodigoUnico() {
        if (this.codigoUnicoEntrada == null) {
            this.codigoUnicoEntrada = UUID.randomUUID();
        }
    }
}
