package com.demo.butaca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    @EqualsAndHashCode.Include
    private Long idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_destinatario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento_relacionado")
    private Evento evento;

    @Column(name = "mensaje_notificacion", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "tipo_notificacion", nullable = false, length = 20)
    private String tipo;

    @Builder.Default
    @Column(name = "leida_notificacion", nullable = false)
    private Boolean leida = false;

    @Column(name = "fecha_creacion_notificacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_envio_notificacion")
    private LocalDateTime fechaEnvio;
}
