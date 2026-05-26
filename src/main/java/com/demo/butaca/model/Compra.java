package com.demo.butaca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "compra")
    private List<Entrada> entradas = new ArrayList<>();

    public Long getIdCompra() {
        return id;
    }

    public void setIdCompra(Long idCompra) {
        this.id = idCompra;
    }

    public Integer getCantidadEntradas() {
        return cantidad;
    }

    public void setCantidadEntradas(Integer cantidadEntradas) {
        this.cantidad = cantidadEntradas == null ? 0 : cantidadEntradas;
    }
}
