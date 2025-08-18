package org.example.Reports;

import org.example.Models.Libro;
import org.example.Models.Prestamo;
import org.example.Services.LibroService;

import java.util.List;
import java.util.UUID;

public class Reportes {

    private List<Libro> libros;
    private List<Prestamo> prestamos;
    private LibroService libroService;

    public Reportes(List<Libro> libros, List<Prestamo> prestamos, LibroService libroService) {
        this.libros = libros;
        this.prestamos = prestamos;
        this.libroService =  libroService;
    }

    public void reporteCompleto() {
        System.out.println("REPORTE COMPLETO DE LIBROS REGISTRADOS: ");
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            System.out.printf("%d: %s - AUTOR: %s %s%n",
                    i + 1,
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");
        }
    }

    public void reporteLibrosDisponibles() {
        System.out.println("REPORTE LIBROS DISPONIBLES: ");
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            if (libro.isDisponible()) {
                System.out.printf("%d: %s - AUTOR: %s %s%n",
                        i + 1,
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");

            }
        }
    }

    public void reporteLibrosPrestados() {
        System.out.println("REPORTE LIBROS PRESTADOS: ");
        for (int i = 0; i < libros.size(); i++) {
            Libro libro = libros.get(i);
            if (!libro.isDisponible()) {
                System.out.printf("%d: %s - AUTOR: %s %s%n",
                        i + 1,
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");

            }
        }
    }

    public void reportePrestamos() {
        System.out.println("REPORTE PRESTAMOS: ");
        for (int i = 0; i < prestamos.size(); i++) {
            Prestamo prestamo = prestamos.get(i);
            System.out.printf("%d: %s - USUARIO: %s %s%n",
                    i + 1,
                    prestamo.getUsuario(),
                    prestamo.getAccion(),
                    prestamo.getFecha().toLocalDate().toString(),
                    this.libroService.buscarLibroPorId(prestamo.getIdLibro()).getTitulo()
            );

        }
    }

    public void reportePrestamoUsuarios(String usuario) {
        System.out.println("REPORTE HISTORIAL POR USUARIO: " + usuario);

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuario().equalsIgnoreCase(usuario)) {
                Libro libro = libroService.buscarLibroPorId(prestamo.getIdLibro());
                System.out.printf("Acción: %s - Fecha: %s - Libro: %s%n",
                        prestamo.getAccion(),
                        prestamo.getFecha().toString(),
                        (libro != null ? libro.getTitulo() : "DESCONOCIDO")
                );
            }
        }
    }



}
