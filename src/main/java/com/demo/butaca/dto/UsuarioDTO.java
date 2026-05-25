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

    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;
}
