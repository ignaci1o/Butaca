package com.demo.butaca.model;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
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
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "apellido_usuario", nullable = false, length = 100)
    private String apellidoUsuario;

    @Column(name = "email_usuario", nullable = false, unique = true, length = 255)
    private String emailUsuario;

    @Column(name = "password_usuario", nullable = false, length = 255)
    private String passwordUsuario;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Builder.Default
    @Column(name = "estado_usuario", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estadoUsuario = EstadoUsuario.ACTIVO;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (estadoUsuario == null) {
            estadoUsuario = EstadoUsuario.ACTIVO;
        }
    }

    public RolUsuario getRolUsuario() {
        if (rol == null || rol.getNombre() == null) {
            return null;
        }
        return RolUsuario.valueOf(rol.getNombre());
    }

    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rol = rolUsuario == null ? null : Rol.builder().nombre(rolUsuario.name()).build();
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario == null ? null : EstadoUsuario.valueOf(estadoUsuario);
    }
}
