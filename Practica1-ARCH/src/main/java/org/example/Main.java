package org.example;

import org.example.Services.Biblioteca;
import org.example.Services.pathArchivos;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                 /$$$$$$$  /$$$$$$ /$$$$$$$  /$$       /$$$$$$  /$$$$$$  /$$$$$$$$ /$$$$$$$$  /$$$$$$   /$$$$$$ 
                | $$__  $$|_  $$_/| $$__  $$| $$      |_  $$_/ /$$__  $$|__  $$__/| $$_____/ /$$__  $$ /$$__  $$
                | $$  \\ $$  | $$  | $$  \\ $$| $$        | $$  | $$  \\ $$   | $$   | $$      | $$  \\__/| $$  \\ $$
                | $$$$$$$   | $$  | $$$$$$$ | $$        | $$  | $$  | $$   | $$   | $$$$$   | $$      | $$$$$$$$
                | $$__  $$  | $$  | $$__  $$| $$        | $$  | $$  | $$   | $$   | $$__/   | $$      | $$__  $$
                | $$  \\ $$  | $$  | $$  \\ $$| $$        | $$  | $$  | $$   | $$   | $$      | $$    $$| $$  | $$
                | $$$$$$$/ /$$$$$$| $$$$$$$/| $$$$$$$$ /$$$$$$|  $$$$$$/   | $$   | $$$$$$$$|  $$$$$$/| $$  | $$
                |_______/ |______/|_______/ |________/|______/ \\______/    |__/   |________/ \\______/ |__/  |__/""");

        String opcion;
        String direccion;
        Biblioteca biblio;
        pathArchivos path;
        System.out.println("QUIERES INGRESAR UN ARCHIVO PARA CARGAR LIBROS? (Y/N)");
        opcion = scanner.nextLine().toLowerCase();
        switch (opcion) {
            case "y":
                System.out.println("INGRESE LA DIRECCION DEL ARCHIVO PARA CARGAR LIBROS");
                direccion = scanner.nextLine();
                path = new pathArchivos();
                biblio = new Biblioteca(direccion, path.getPathArchivo());
                biblio.menu();
                break;
            case "n":
                path = new pathArchivos();
                biblio = new Biblioteca("", path.getPathArchivo());
                biblio.menu();
                break;
            default:
                System.out.println("OPCION INVALIDA SE INGRESA SIN ARCHIVO");
                path = new pathArchivos();
                biblio = new Biblioteca("", path.getPathArchivo());
                biblio.menu();
                break;
        }
    }

}