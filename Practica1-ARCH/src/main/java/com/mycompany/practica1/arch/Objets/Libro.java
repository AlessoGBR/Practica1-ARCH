/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.arch.Objets;

import java.util.UUID;

/**
 *
 * @author alesso
 */
public class Libro {
    
    private UUID id;
    private String autor;
    private String titulo;
    private int fechaPublicacion;
    private boolean disponible;

    public UUID getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getFechaPublicacion() {
        return fechaPublicacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setFechaPublicacion(int fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    
}
