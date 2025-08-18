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

        System.out.println("Ingrese la direccion del archivo para ingresar libros");
        String direccion = scanner.nextLine();
        pathArchivos path = new pathArchivos();
        Biblioteca biblio = new Biblioteca(direccion, path.getPathArchivo());
        biblio.menu();
    }
}