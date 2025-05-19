package com.taskea.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private boolean completada;
    private String encargado;
    private LocalDateTime fechaCreacion;
    private Integer periodicidad; // número de días
    private LocalDateTime fechaCompletada;
    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(LocalDateTime fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }
    public Turno getTurno() {
        return turno;
    }
    public void setTurno(Turno turno) {
        this.turno = turno;
    }
    public Integer getPeriodicidad() {
        return periodicidad;
    }
    public void setPeriodicidad(Integer periodicidad) {
        this.periodicidad = periodicidad;
    }
}