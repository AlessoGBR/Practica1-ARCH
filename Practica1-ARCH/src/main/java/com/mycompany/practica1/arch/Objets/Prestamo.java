/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.arch.Objets;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author alesso
 */
public class Prestamo {
    
    private UUID idLibro;
    private String Usuario;
    private String estado;
    private Date fecha;

    public Prestamo(UUID idLibro, String usuario, String estado){
        this.idLibro = idLibro;
        this.Usuario = usuario;
        this.estado = estado;
    }
    
    public UUID getIdLibre() {
        return idLibro;
    }

    public void setIdLibre(UUID idLibro) {
        this.idLibro = idLibro;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
