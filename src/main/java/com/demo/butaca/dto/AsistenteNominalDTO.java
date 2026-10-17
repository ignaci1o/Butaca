package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsistenteNominalDTO {
    @JsonProperty("dni_asistente")
    private String dniAsistente;

    @JsonProperty("nombre_asistente")
    private String nombreAsistente;
}
