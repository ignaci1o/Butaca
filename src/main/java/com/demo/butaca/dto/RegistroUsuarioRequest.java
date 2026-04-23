package com.demo.butaca.dto;

import com.demo.butaca.model.RolUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistroUsuarioRequest {
    @NotBlank
    @Size(max = 100)
    @JsonProperty("nombre_usuario")
    private String nombreUsuario;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("apellido_usuario")
    private String apellidoUsuario;

    @NotBlank
    @Email
    @Size(max = 150)
    @JsonProperty("email_usuario")
    private String emailUsuario;

    @NotBlank
    @Size(min = 6, max = 100)
    @JsonProperty("password_usuario")
    private String passwordUsuario;

    @NotNull
    @JsonProperty("rol_usuario")
    private RolUsuario rolUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
}
