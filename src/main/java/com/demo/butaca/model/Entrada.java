package com.demo.butaca.model;

public class Entrada {
    private Long id;
    private Long eventoId;
    private String codigoUnico;
    private boolean usada;

    public Entrada() {
    }

    public Entrada(Long id, Long eventoId, String codigoUnico, boolean usada) {
        this.id = id;
        this.eventoId = eventoId;
        this.codigoUnico = codigoUnico;
        this.usada = usada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public boolean isUsada() {
        return usada;
    }

    public void setUsada(boolean usada) {
        this.usada = usada;
    }
}
