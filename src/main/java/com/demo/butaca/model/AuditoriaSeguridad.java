package com.demo.butaca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "auditoria_seguridad")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaSeguridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    @EqualsAndHashCode.Include
    private Long idAuditoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "accion_auditoria", nullable = false, length = 50)
    private String accionAuditoria;

    @Column(name = "entidad_afectada", nullable = false, length = 50)
    private String entidadAfectada;

    @Column(name = "id_registro_afectado")
    private Long idRegistroAfectado;

    @Builder.Default
    @Column(name = "resultado_auditoria", nullable = false, length = 20)
    private String resultadoAuditoria = "EXITOSO";

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "detalle_tracking", nullable = false, length = 500)
    private String detalleTracking;

    @Column(name = "fecha_auditoria", nullable = false, updatable = false)
    private LocalDateTime fechaAuditoria;

    @PrePersist
    public void prePersistFecha() {
        if (fechaAuditoria == null) {
            fechaAuditoria = LocalDateTime.now();
        }
    }
}
