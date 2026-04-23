package com.demo.butaca.dto;

import com.demo.butaca.model.RolUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    @JsonProperty("id_usuario")
    private Long idUsuario;

    @JsonProperty("nombre_usuario")
    private String nombreUsuario;

    @JsonProperty("apellido_usuario")
    private String apellidoUsuario;

    @JsonProperty("email_usuario")
    private String emailUsuario;

    @JsonProperty("rol_usuario")
    private RolUsuario rolUsuario;

    private String mensaje;

    public AuthResponse() {
    }

    public AuthResponse(Long idUsuario, String nombreUsuario, String apellidoUsuario, String emailUsuario, RolUsuario rolUsuario, String mensaje) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.rolUsuario = rolUsuario;
        this.mensaje = mensaje;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
