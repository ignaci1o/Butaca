package com.demo.butaca.model;

import java.time.LocalDate;

public class Evento {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private int capacidad;
    private int entradasDisponibles;

    public Evento() {
    }

    public Evento(Long id, String nombre, LocalDate fecha, int capacidad, int entradasDisponibles) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.capacidad = capacidad;
        this.entradasDisponibles = entradasDisponibles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getEntradasDisponibles() {
        return entradasDisponibles;
    }

    public void setEntradasDisponibles(int entradasDisponibles) {
        this.entradasDisponibles = entradasDisponibles;
    }
}
