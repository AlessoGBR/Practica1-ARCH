package org.example.Services;

import lombok.Getter;
import org.example.Models.Libro;
import org.example.Models.Prestamo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibroService {

    @Getter
    private List<Libro> libros;
    @Getter
    private List<Prestamo> prestamos;
    private final String filePath;

    public LibroService(String filePath) {
        this.libros = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.filePath = filePath + File.separator + "libros.gbr";
        File file = new File(filePath);
        if (file.exists()) {
            loadData();
        } else {
            System.out.println("NO HAY LIBROS GUARDADOS");
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
                System.out.println("SE CARGARON LOS LIBROS CORRECTAMENTE");
            } catch (Exception e) {
                System.out.println("OCURRIO UN ERROR A LA HORA DE CARGAR DATOS: " + e.getMessage());
            }
        } else {
            System.out.println("EL ARCHIVO DE LIBROS NO EXISTE");
        }

    }

    public void importarDesdeCSV(String csvPath) {
        int lineaNumero = 0;
        int librosImportados = 0;
        int erroresEncontrados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String linea;
            boolean primera = true;

            while ((linea = br.readLine()) != null) {
                lineaNumero++;

                if (primera) {
                    primera = false;
                    continue;
                }

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] valores = parsearLineaCSV(linea);

                if (valores.length != 5) {
                    erroresEncontrados++;
                    continue;
                }

                ResultadoValidacion resultado = validarYCrearLibro(valores, lineaNumero);

                if (resultado.esValido) {
                    if (existeLibroPorId(resultado.libro.getId())) {
                        continue;
                    }

                    libros.add(resultado.libro);
                    librosImportados++;
                } else {
                    erroresEncontrados++;
                }
            }

            if (librosImportados > 0) {
                saveInFile();
            }

            System.out.println("=========================================");
            System.out.println("LIBROS IMPORTADOS: " + librosImportados);
            System.out.println("ERRORES ENCONTRADOS: " + erroresEncontrados);
            System.out.println("TOTAL PROCESAMIENTO: " + (lineaNumero - 1));

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: NO SE ENCONTRO EL ARHCHIVO " + csvPath);
        } catch (IOException e) {
            System.out.println("ERROR AL LEER EL ARCHIVO CSV: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] parsearLineaCSV(String linea) {
        return linea.split(",", -1);
    }

    private ResultadoValidacion validarYCrearLibro(String[] valores, int lineaNumero) {
        try {
            String rawId = valores[0].trim();
            if (rawId.isEmpty()) {
                return new ResultadoValidacion(false, "ID NULO");
            }

            UUID id;
            try {
                id = UUID.fromString(rawId);
            } catch (IllegalArgumentException e) {
                return new ResultadoValidacion(false, "ID INVALIDO: " + rawId);
            }

            String titulo = valores[1].trim();
            if (titulo.isEmpty()) {
                return new ResultadoValidacion(false, "SIN TITULO");
            }
            if (titulo.length() > 200) {
                return new ResultadoValidacion(false, "TITULO MUY LARGO");
            }

            String autor = valores[2].trim();
            if (autor.isEmpty()) {
                return new ResultadoValidacion(false, "AUTOR NULO");
            }
            if (autor.length() > 100) {
                return new ResultadoValidacion(false, "NOMBRE MUY LARGO");
            }

            String rawAnio = valores[3].trim();
            if (rawAnio.isEmpty()) {
                return new ResultadoValidacion(false, "ANIO VACIO");
            }

            int anio;
            try {
                anio = Integer.parseInt(rawAnio);
            } catch (NumberFormatException e) {
                return new ResultadoValidacion(false, "ANIO INVALIDO: " + rawAnio);
            }

            int anioActual = java.time.Year.now().getValue();
            if (anio < -3000 || anio > anioActual + 5) {
                return new ResultadoValidacion(false, "ANIO FUERA DE RANGO " + (anioActual + 5) + anio);
            }

            String rawDisponible = valores[4].trim().toLowerCase();
            if (rawDisponible.isEmpty()) {
                return new ResultadoValidacion(false, "DISPONIBLE NULO");
            }

            boolean disponible;
            switch (rawDisponible) {
                case "true":
                    disponible = true;
                    break;
                case "false":
                    disponible = false;
                    break;
                default:
                    return new ResultadoValidacion(false, "DISPONIBLE INVALIDO: " + valores[4].trim());
            }

            Libro libro = new Libro(id, titulo, autor, anio, disponible);
            if (!libro.isDisponible()){
                Prestamo prestamo = new Prestamo(libro.getId(), "PREDETERMINADO", "PRESTAMO");
                this.prestamos.add(prestamo);
            }
            return new ResultadoValidacion(true, libro, "");

        } catch (Exception e) {
            return new ResultadoValidacion(false, "ERROR INESPERADO: " + e.getMessage());
        }
    }

    private boolean existeLibroPorId(UUID id) {
        return libros.stream().anyMatch(libro -> libro.getId().equals(id));
    }

    private static class ResultadoValidacion {
        final boolean esValido;
        final Libro libro;
        final String mensajeError;

        public ResultadoValidacion(boolean esValido, String mensajeError) {
            this.esValido = esValido;
            this.libro = null;
            this.mensajeError = mensajeError;
        }

        public ResultadoValidacion(boolean esValido, Libro libro, String mensajeError) {
            this.esValido = esValido;
            this.libro = libro;
            this.mensajeError = mensajeError;
        }
    }

}
