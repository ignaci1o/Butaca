package com.demo.butaca.dto;

import com.demo.butaca.model.EstadoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty("id_usuario")
    private Long idUsuario;

    @JsonProperty("nombre_usuario")
    private String nombreUsuario;

    @JsonProperty("apellido_usuario")
    private String apellidoUsuario;

    @JsonProperty("dni_usuario")
    private String dniUsuario;

    @JsonProperty("email_usuario")
    private String emailUsuario;

    @JsonProperty("rol_usuario")
    private String rolUsuario;

    private String telefono;

    @JsonProperty("estado_usuario")
    private EstadoUsuario estadoUsuario;

    @JsonProperty("identidad_verificada")
    private Boolean identidadVerificada;

    @JsonProperty("estado_verificacion")
    private String estadoVerificacion;

    private String mensaje;
}
