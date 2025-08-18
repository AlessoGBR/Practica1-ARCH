package org.example.Models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "idLibro")
public class Prestamo implements Serializable {

    private UUID idLibro;
    private String usuario;
    private String accion;
    private LocalDateTime fecha;

    public Prestamo(UUID idLibro, String usuario, String accion) {
        this.idLibro = idLibro;
        this.usuario = usuario;
        this.accion = accion;
        this.fecha = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "USUARIO: " + usuario + " ACCION REALIZADA: " + accion + "\n" + "FECHA: " + fecha;
    }
}
