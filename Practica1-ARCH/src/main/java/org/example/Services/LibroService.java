package org.example.Services;

import lombok.Getter;
import org.example.Models.Libro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibroService {

    @Getter
    private List<Libro> libros;
    private final String filePath;

    public LibroService(String filePath) {
        this.libros = new ArrayList<>();
        this.filePath = filePath + File.separator + "libros.txt";
        File file = new File(filePath);
        if (file.exists()) {
            loadData();
        } else {
            System.out.println("NO HAY DATOS GUARDADOS");
        }
    }

    public void addLibro(Libro libro) {
        libros.add(libro);
        saveInFile();
    }

    public Libro buscarLibroPorId(UUID id) {
        for (Libro libro : libros) {
            if (libro.getId().equals(id)) {
                return libro;
            }
        }
        return null;
    }

    public List<Libro> buscarLibroPorTitulo(String titulo) {
        List<Libro> librosAutor = new ArrayList<>();
        for (Libro libro : libros) {
            if (libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                librosAutor.add(libro);
            }
        }
        return librosAutor;
    }

    public void updateLibro(Libro libro) {
        saveInFile();
    }

    private void saveInFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(this.libros);
        } catch (IOException e) {
            System.out.println("NO SE PUDO GUARDAR LA LSITA" + e.getMessage());
        }
    }

    private List<Libro> returnlibros(Object object) {
        if (!(object instanceof List<?>)) {
            return null;
        }
        final List<?> list = (List<?>) object;
        for (Object item : list) {
            if (!(item instanceof Libro)) {
                return null;
            }
        }
        return (List<Libro>) list;
    }

    private void loadData() {
        final File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
                final Object object = objectInputStream.readObject();
                final List<Libro> list = returnlibros(object);
                if (list == null) {
                    System.out.println("NO ES UNA LISTA DE LIBROS");
                    return;
                }
                this.libros = list;
                System.out.println("SE CARGARON LOS DATOS CORRECTAMENTE");
            } catch (Exception e) {
                System.out.println("OCURRIO UN ERROR A LA HORA DE CARGAR DATOS: " + e.getMessage());
            }
        } else {
            System.out.println("EL ARCHIVO DE PERSISTENCIA NO EXISTE");
        }

    }

    public void importarDesdeCSV(String csvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String linea;
            boolean primera = true;

            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }

                String[] valores = linea.split(",");
                if (valores.length != 5) {
                    System.out.println("LÍNEA INVÁLIDA: " + linea);
                    continue;
                }

                UUID id;
                try {
                    id = UUID.fromString(valores[0].trim());
                } catch (IllegalArgumentException e) {
                    id = UUID.randomUUID();
                }
                String titulo = valores[1].trim();
                String autor = valores[2].trim();
                int anio = Integer.parseInt(valores[3].trim());
                boolean disponible = Boolean.parseBoolean(valores[4].trim());

                Libro libro = new Libro(id, titulo, autor, anio, disponible);
                libros.add(libro);
            }

            saveInFile();
            System.out.println("IMPORTACIÓN DESDE CSV COMPLETADA");
        } catch (Exception e) {
            System.out.println("ERROR AL IMPORTAR CSV: " + e.getMessage());
        }
    }

}
