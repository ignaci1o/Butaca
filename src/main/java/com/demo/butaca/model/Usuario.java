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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_email", columnNames = "email_usuario"))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @EqualsAndHashCode.Include
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "apellido_usuario", nullable = false, length = 100)
    private String apellidoUsuario;

    @Column(name = "email_usuario", nullable = false, unique = true, length = 255)
    private String emailUsuario;

    @Column(name = "password_hash_usuario", nullable = false, length = 255)
    private String passwordUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "telefono_usuario", length = 20)
    private String telefono;

    @Builder.Default
    @Column(name = "estado_usuario", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estadoUsuario = EstadoUsuario.ACTIVO;

    public RolUsuario getRolUsuario() {
        if (rol == null || rol.getNombre() == null) {
            return null;
        }
        String nombre = rol.getNombre();
        return RolUsuario.valueOf("CLIENT".equals(nombre) ? "CLIENTE" : nombre);
    }

    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rol = rolUsuario == null ? null : Rol.builder().nombre(rolUsuario.name()).build();
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario == null ? null : EstadoUsuario.valueOf(estadoUsuario);
    }
}
