package org.example.Services;

import org.example.Models.Libro;
import org.example.Models.Prestamo;
import org.example.Reports.Reportes;

import java.util.*;

public class Biblioteca {

    private String path;
    private List<Libro> libros;
    private List<Prestamo> prestamos;
    private String pathArchivo;
    private Scanner sc = new Scanner(System.in);
    LibroService libroService;
    PrestarService prestarService;
    private Reportes reportes;

    public Biblioteca(String path, String direccion) {
        this.path = path;
        this.pathArchivo = direccion;
        this.libroService = new LibroService(pathArchivo);
        libroService.importarDesdeCSV(path);
        this.libros = libroService.getLibros();
        this.prestarService = new PrestarService(pathArchivo);
        this.prestamos = prestarService.getPrestamos();
        this.reportes = new Reportes(libros, prestamos, libroService);
    }

    public void menu() {
        System.out.println("=================");
        System.out.println("MENU BIBLIOTECA");
        System.out.println("1. AGREGAR LIBRO");
        System.out.println("2. PRESTAR LIBRO");
        System.out.println("3. BUSCAR LIBRO");
        System.out.println("4. DEVOLVER LIBRO");
        System.out.println("5. REPORTES DE BIBLITECA");
        System.out.println("0. SALIR");
        String eleccion = sc.nextLine();
        switch (eleccion) {
            case "1":
                agregarLibro();
                break;
            case "2":
                prestarLibro();
                break;
            case "3":
                buscarLibro();
                break;
            case "4":
                devolverLibro();
                break;
            case "5":
                reportes();
                break;
            case "0":
                System.exit(0);
                break;
            default:
                System.out.println("OPCION INCORRECTA");
                menu();
                break;
        }
    }

    private void agregarLibro() {
        System.out.println("=================");
        System.out.println("INGRESA EL NOMBRE DEL LIBRO");
        String nombreLibro = sc.nextLine();
        System.out.println("INGRESA EL AUTOR DEL LIBRO");
        String autor = sc.nextLine();
        System.out.println("INGRESA EL FECHA DEL LIBRO");
        int fechaPublicacion = sc.nextInt();
        Libro libro = new Libro(nombreLibro, autor, fechaPublicacion);
        this.libroService.addLibro(libro);
    }

    private void prestarLibro() {
        System.out.println("=================");
        System.out.println("COMO QUIERES BUSCAR EL LIBRO?");
        System.out.println("1. POR ID");
        System.out.println("2. POR NOMBRE");
        String opcion = sc.nextLine();
        Libro libroAPrestar;
        switch (opcion) {
            case "1":
                System.out.println("=================");
                System.out.println("INGRESA EL ID DEL LIBRO");
                UUID id = UUID.fromString(sc.nextLine());
                libroAPrestar = this.libroService.buscarLibroPorId(id);
                if (libroAPrestar != null) {
                    if (libroAPrestar.isDisponible()) {
                        System.out.println("=================");
                        System.out.println("INGRESA EL NOMBRE DEL USUARIO QUE REGISTRA EL PRESTAMO: ");
                        String nombreUsuario = sc.nextLine();
                        libroAPrestar.setDisponible(false);
                        Prestamo prestamo = new Prestamo(libroAPrestar.getId(), nombreUsuario, "PRESTAMO");
                        this.prestarService.addprestamos(prestamo);
                        libroService.updateLibro(libroAPrestar);
                        System.out.println("LIBRO PRESTADO");
                    } else {
                        System.out.println("LIBRO NO DISPONIBLE");
                    }
                } else {
                    System.out.println("NO SE ENCONTRO EL LIBRO");
                }
                menu();
                break;
            case "2":
                System.out.println("=================");
                System.out.println("INGRESA EL NOMBRE DEL LIBRO");
                String nombreLibro = sc.nextLine().trim();
                if (nombreLibro.isEmpty()) {
                    System.out.println("DEBES DE INGRESAR UN NOMBRE VALIDO");
                    break;
                }
                List<Libro> libros = this.libroService.buscarLibroPorTitulo(nombreLibro);
                if (libros.isEmpty()) {
                    System.out.println("NO SE ENCONTRARON LIBROS");
                    break;
                }
                if (libros.size() > 1) {
                    System.out.println("=================");
                    System.out.println("MULTIPLES LIBROS ENCONTRADOS, CUAL DESEAS PRESTAR?");
                    for (int i = 0; i < libros.size(); i++) {
                        Libro libro = libros.get(i);
                        System.out.printf("%d: %s - AUTOR: %s %s%n",
                                i + 1,
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");
                    }
                    try {
                        int indice = Integer.parseInt(sc.nextLine()) - 1;
                        if (indice < 0 || indice >= libros.size()) {
                            System.out.println("SELECCION INVALIDA");
                            break;
                        }
                        Libro libroSeleccionado = libros.get(indice);
                        if (libroSeleccionado.isDisponible()) {
                            System.out.println("=================");
                            System.out.println("INGRESA EL NOMBRE DEL USUARIO QUE REGISTRA EL PRESTAMO: ");
                            String usuario = sc.nextLine();
                            libroSeleccionado.setDisponible(false);
                            Prestamo prestamo = new Prestamo(libroSeleccionado.getId(), usuario, "PRESTAMO");
                            this.prestarService.addprestamos(prestamo);
                            libroService.updateLibro(libroSeleccionado);
                            System.out.println("LIBRO PRESTADO CON ÉXITO: " + libroSeleccionado.getTitulo());
                        } else {
                            System.out.println("LIBRO NO DISPONIBLE");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("DEBES DE INGRESAR UN NUMERO VALIDO");
                    }
                } else {
                    Libro libro = libros.get(0);
                    if (libro.isDisponible()) {
                        System.out.println("=================");
                        System.out.println("INGRESA EL NOMBRE DEL USUARIO QUE REGISTRA EL PRESTAMO: ");
                        String usuario = sc.nextLine();
                        libro.setDisponible(false);
                        Prestamo prestamo = new Prestamo(libro.getId(), usuario, "PRESTAMO");
                        this.prestarService.addprestamos(prestamo);
                        libroService.updateLibro(libro);
                        System.out.println("LIBRO PRESTADO CON EXITO: " + libro.getTitulo());
                    } else {
                        System.out.println("LIBRO NO DISPONIBLE");
                    }
                }
                menu();
                break;
            default:
                System.out.println("OPCION INCORRECTA");
                break;
        }

    }

    private void buscarLibro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================");
        System.out.println("COMO DESEA BUSCAR EL LIBRO");
        System.out.println("1. POR NOMBRE DEL LIBRO");
        System.out.println("2. POR ID");
        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1:
                System.out.println("INGRESA EL NOMBRE DEL LIBRO");
                String titulo = sc.nextLine();
                List<Libro> librosAutor = libroService.buscarLibroPorTitulo(titulo);
                if (librosAutor.isEmpty()) {
                    System.out.println("NO SE ENCONTRARON LIBROS");
                    menu();
                } else {
                    System.out.println("=================");
                    System.out.println("LIBROS ENCONTRADOS");
                    for (int i = 0; i < librosAutor.size(); i++) {
                        Libro libro = librosAutor.get(i);
                        System.out.printf("%d: %s - AUTOR: %s %s%n",
                                i + 1,
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");
                    }
                    menu();
                }
                break;
            case 2:
                System.out.println("INGRESA EL ID DEL LIBRO: ");
                UUID id = UUID.fromString(sc.nextLine());
                Libro librosId = libroService.buscarLibroPorId(id);
                if (librosId != null) {
                    System.out.println("LIBRO ENCONTRADO: ");
                    System.out.println(librosId.toString());
                    menu();
                } else {
                    System.out.println("LIBRO NO ENCONTRADO");
                    menu();
                }
                break;
            default:
                System.out.println("OPCION INCORRECTA");
                menu();
                break;
        }
    }

    private void devolverLibro() {
        System.out.println("=================");
        System.out.println("COMO QUIERES BUSCAR EL LIBRO?");
        System.out.println("1. POR ID");
        System.out.println("2. POR NOMBRE");
        String opcion = sc.nextLine();
        Libro libroAPrestar;
        switch (opcion) {
            case "1":
                System.out.println("=================");
                System.out.println("INGRESA EL NOMBRE DEL USUARIO QUE REGISTRA LA DEVOLUCION: ");
                String nombreUsuario = sc.nextLine();
                System.out.println("INGRESA EL ID DEL LIBRO");
                UUID id = UUID.fromString(sc.nextLine());
                libroAPrestar = this.libroService.buscarLibroPorId(id);
                if (libroAPrestar != null) {
                    if (!libroAPrestar.isDisponible()) {
                        libroAPrestar.setDisponible(true);
                        Prestamo prestamo = new Prestamo(libroAPrestar.getId(), nombreUsuario, "DEVOLUCION");
                        this.prestarService.addprestamos(prestamo);
                        libroService.updateLibro(libroAPrestar);
                        System.out.println("LIBRO DEVUELTO");
                    } else {
                        System.out.println("LIBRO NO ESTA PRESTADO");
                    }
                } else {
                    System.out.println("NO SE ENCONTRO EL LIBRO");
                }
                menu();
                break;
            case "2":
                System.out.println("=================");
                System.out.println("INGRESA EL NOMBRE DEL LIBRO");
                String nombreLibro = sc.nextLine().trim();
                if (nombreLibro.isEmpty()) {
                    System.out.println("DEBES DE INGRESAR UN NOMBRE VALIDO");
                    break;
                }
                List<Libro> libros = this.libroService.buscarLibroPorTitulo(nombreLibro);
                if (libros.isEmpty()) {
                    System.out.println("NO SE ENCONTRARON LIBROS");
                    break;
                }
                if (libros.size() > 1) {
                    System.out.println("=================");
                    System.out.println("MULTIPLES LIBROS ENCONTRADOS, CUAL DESEAS DEVOLVER?");
                    for (int i = 0; i < libros.size(); i++) {
                        Libro libro = libros.get(i);
                        System.out.printf("%d: %s - AUTOR: %s %s%n",
                                i + 1,
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");
                    }
                    try {
                        int indice = Integer.parseInt(sc.nextLine()) - 1;
                        if (indice < 0 || indice >= libros.size()) {
                            System.out.println("SELECCION INVALIDA");
                            break;
                        }
                        Libro libroSeleccionado = libros.get(indice);
                        if (!libroSeleccionado.isDisponible()) {
                            System.out.println("=================");
                            System.out.println("INGRESA EL NOMBRE DEL USUARIO QUE REGISTRA LA DEVOLUCION: ");
                            String usuario = sc.nextLine();
                            libroSeleccionado.setDisponible(true);
                            Prestamo prestamo = new Prestamo(libroSeleccionado.getId(), usuario, "DEVOLUCION");
                            this.prestarService.addprestamos(prestamo);
                            libroService.updateLibro(libroSeleccionado);
                            System.out.println("LIBRO DEVUELTO");
                        } else {
                            System.out.println("LIBRO NO ESTA PRESTADO");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("DEBES DE INGRESAR UN NUMERO VALIDO");
                    }
                } else {
                    Libro libro = libros.get(0);
                    if (!libro.isDisponible()) {
                        System.out.println("=================");
                        System.out.println("INGRESA EL NOMBRE DEL USUARIO QUE REGISTRA LA DEVOLUCION: ");
                        String usuario = sc.nextLine();
                        libro.setDisponible(true);
                        Prestamo prestamo = new Prestamo(libro.getId(), usuario, "DEVOLUCION");
                        this.prestarService.addprestamos(prestamo);
                        libroService.updateLibro(libro);
                        System.out.println("LIBRO DEVUELTO");
                    } else {
                        System.out.println("LIBRO NO ESTA PRESTADO");
                    }
                }
                menu();
                break;
            default:
                System.out.println("OPCION INCORRECTA");
                break;
        }
    }

    private void reportes() {
        System.out.println("=================");
        System.out.println("QUE REPORTE DESEAS VER?");
        System.out.println("1. REPORTE COMPLETO DE LIBROS");
        System.out.println("2. REPORTE LIBROS DISPONIBLES");
        System.out.println("3. REPORTE LIBROS PRESTADOS");
        System.out.println("4. REPORTE PRESTAMOS");
        System.out.println("5. REPORTE PRESTAMOS POR LIBRO");
        System.out.println("6. REPORTE PRESTAMOS POR USUARIO");
        System.out.println("0. REGRESAR");
        String opcion = sc.nextLine();
        switch (opcion) {
            case "1":
                System.out.println("=================");
                this.reportes.reporteCompleto();
                reportes();
                break;
            case "2":
                System.out.println("=================");
                this.reportes.reporteLibrosDisponibles();
                reportes();
                break;
            case "3":
                System.out.println("=================");
                this.reportes.reporteLibrosPrestados();
                reportes();
                break;
            case "4":
                System.out.println("=================");
                this.reportes.reportePrestamos();
                reportes();
                break;
            case "5":
                System.out.println("=================");
                reportes();
                break;
            case "6":
                System.out.println("=================");
                System.out.println("INGRESA EL NOMBRE DEL USUARIO: ");
                String usuario = sc.nextLine();
                this.reportes.reportePrestamoUsuarios(usuario);
                reportes();
                break;
            case "0":
                menu();
                break;
            default:
                System.out.println("OPCION INCORRECTA");
                reportes();
                break;
        }
    }


}
