package org.example.Reports;

import org.example.Models.Libro;
import org.example.Models.Prestamo;
import org.example.Services.LibroService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Reportes {

    private List<Libro> libros;
    private List<Prestamo> prestamos;
    private LibroService libroService;

    public Reportes(List<Libro> libros, List<Prestamo> prestamos, LibroService libroService) {
        this.libros = libros;
        this.prestamos = prestamos;
        this.libroService = libroService;
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
            System.out.printf("USUARIO: %d: %s - TIPO: %s %s%n",
                    i + 1,
                    prestamo.getUsuario(),
                    prestamo.getAccion(),
                    prestamo.getFecha().toLocalDate().toString(),
                    this.libroService.buscarLibroPorId(prestamo.getIdLibro()).getTitulo()
            );

        }
    }

    public void reportePrestamosLibros() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("PRESTAMOS POR LIBRO");
        System.out.println("1. POR TITULO");
        System.out.println("2. POR ID");
        int opcion;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("OPCION INVALIDA");
            return;
        }
        UUID idLibro = null;
        switch (opcion) {
            case 1:
                System.out.println("INGRESE EL TITULO DEL LIBRO: ");
                String titulo = scanner.nextLine().trim();
                List<Libro> librosTitulo = libroService.buscarLibroPorTitulo(titulo);
                if (librosTitulo.isEmpty()) {
                    System.out.println("NO SE ENCONTRARON LIBROS");
                    return;
                }
                if (librosTitulo.size() > 1) {
                    System.out.println("SE ENCONTRARON VARIOS LIBROS: ");
                    for (int i = 0; i < librosTitulo.size(); i++) {
                        Libro libro = librosTitulo.get(i);
                        System.out.printf("%d. %s - Autor: %s - %s%n",
                                i + 1,
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");
                    }
                    System.out.print("INGRESE EL NUMERO DEL LIBRO");
                    try {
                        int seleccion = Integer.parseInt(scanner.nextLine());
                        if (seleccion < 1 || seleccion > librosTitulo.size()) {
                            System.out.println("SELECCION INVALIDA");
                            return;
                        }
                        idLibro = librosTitulo.get(seleccion - 1).getId();
                    } catch (NumberFormatException e) {
                        System.out.println("ENTRADA INVALIDA");
                        return;
                    }
                } else {
                    idLibro = librosTitulo.get(0).getId();
                }
                break;
            case 2:
                System.out.println("INGRESE EL ID DEL LIBRO");
                String idInput = scanner.nextLine().trim();
                try {
                    idLibro = UUID.fromString(idInput);
                } catch (IllegalArgumentException e) {
                    System.out.println("ID INVALIDO");
                    return;
                }
                break;
            default:
                System.out.println("OPCION INVALIDA");
                return;
        }
        Libro libroSeleccionado = libroService.buscarLibroPorId(idLibro);
        if (libroSeleccionado == null) {
            System.out.println("NO SE ENCONTRO EL LIBRO");
            return;
        }
        System.out.println("HISTORIAL DE PRESTAMOS DE: " + libroSeleccionado.getTitulo());
        boolean encontrado = false;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getIdLibro().equals(idLibro)) {
                encontrado = true;
                System.out.printf("USUARIO: %s | ACCION: %s | FECHA: %s%n",
                        prestamo.getUsuario(),
                        prestamo.getAccion(),
                        prestamo.getFecha().toString());
            }
        }
        if (!encontrado) {
            System.out.println("EL LIBRO NO CUENTA CON PRESTAMOS");
        }
    }


    public void reportePrestamoUsuarios(String usuario) {
        System.out.println("REPORTE HISTORIAL POR USUARIO: " + usuario);

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuario().equalsIgnoreCase(usuario)) {
                Libro libro = libroService.buscarLibroPorId(prestamo.getIdLibro());
                System.out.printf("ACCION: %s - FECHA: %s - LIBRO: %s%n",
                        prestamo.getAccion(),
                        prestamo.getFecha().toString(),
                        (libro != null ? libro.getTitulo() : "DESCONOCIDO")
                );
            }
        }
    }


}
