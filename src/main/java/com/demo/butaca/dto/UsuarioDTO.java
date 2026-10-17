package com.demo.butaca.dto;

import com.demo.butaca.model.EstadoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    @JsonProperty("id_usuario")
    private Long idUsuario;

    @NotBlank
    @JsonProperty("nombre_usuario")
    private String nombreUsuario;

    @NotBlank
    @JsonProperty("apellido_usuario")
    private String apellidoUsuario;

    @JsonProperty("dni_usuario")
    private String dniUsuario;

    @Email
    @NotBlank
    @JsonProperty("email_usuario")
    private String emailUsuario;

    private String telefono;

    @JsonProperty("estado_usuario")
    private EstadoUsuario estadoUsuario;

    @JsonProperty("rol_id")
    private Long rolId;

    @JsonProperty("rol_usuario")
    private String rolUsuario;

    @JsonProperty("identidad_verificada")
    private Boolean identidadVerificada;

    @JsonProperty("estado_verificacion")
    private String estadoVerificacion;

    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
