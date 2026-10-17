package com.demo.butaca.dto;

import com.demo.butaca.model.EstadoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroUsuarioRequest {
    @NotBlank
    @Size(max = 100)
    @JsonProperty("nombre_usuario")
    private String nombreUsuario;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("apellido_usuario")
    private String apellidoUsuario;

    @Size(max = 20)
    @JsonProperty("dni_usuario")
    private String dniUsuario;

    @NotBlank
    @Email
    @Size(max = 150)
    @JsonProperty("email_usuario")
    private String emailUsuario;

    @NotBlank
    @Size(min = 6, max = 100)
    @JsonProperty("password_usuario")
    private String passwordUsuario;

    @JsonProperty("rol_usuario")
    private String rolUsuario;

    private String telefono;

    @JsonProperty("estado_usuario")
    private EstadoUsuario estadoUsuario;
}
