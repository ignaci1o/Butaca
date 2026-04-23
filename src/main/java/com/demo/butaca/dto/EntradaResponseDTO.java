package com.demo.butaca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class EntradaResponseDTO {

    @JsonProperty("id_entrada")
    private Long idEntrada;

    @JsonProperty("codigo_unico_entrada")
    private UUID codigoUnicoEntrada;

    @JsonProperty("estado_entrada")
    private String estadoEntrada;

    @JsonProperty("evento_id")
    private Long eventoId;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    public Long getIdEntrada() { return idEntrada; }
    public void setIdEntrada(Long idEntrada) { this.idEntrada = idEntrada; }
    public UUID getCodigoUnicoEntrada() { return codigoUnicoEntrada; }
    public void setCodigoUnicoEntrada(UUID codigoUnicoEntrada) { this.codigoUnicoEntrada = codigoUnicoEntrada; }
    public String getEstadoEntrada() { return estadoEntrada; }
    public void setEstadoEntrada(String estadoEntrada) { this.estadoEntrada = estadoEntrada; }
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
