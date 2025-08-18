package org.example.Models;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Libro implements Serializable {

    private final UUID id;
    private String titulo;
    private String autor;
    private int fechaPublicacion;
    private boolean disponible;

    public Libro(UUID id, String titulo, String autor, int fechaPublicacion, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.disponible = disponible;

    }

    public Libro(String titulo, String autor, int fechaPublicacion) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.disponible = false;
    }

    @Override
    public String toString() {
        return "TITULO: " + this.titulo + " - AUTOR " + this.autor + " - FECHA PUBLICACION " + this.fechaPublicacion;
    }
}
