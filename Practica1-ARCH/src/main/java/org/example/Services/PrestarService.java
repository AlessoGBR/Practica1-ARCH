package org.example.Services;

import lombok.Getter;
import org.example.Models.Libro;
import org.example.Models.Prestamo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrestarService {

    @Getter
    private List<Prestamo> prestamos;
    private final String filePath;

    public PrestarService(String filePath) {
        this.prestamos = new ArrayList<>();
        this.filePath = filePath + File.separator + "prestar.txt";
        File file = new File(filePath);
        if (file.exists()) {
            loadData();
        } else {
            System.out.println("NO HAY DATOS GUARDADOS");
        }
    }

    public void addprestamos(Prestamo prestamo) {
        prestamos.add(prestamo);
        saveInFile();
    }

    public void updateprestamos(Prestamo prestamo) {
        saveInFile();
    }

    private void saveInFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(this.prestamos);
        } catch (IOException e) {
            System.out.println("NO SE PUDO GUARDAR LA LSITA" + e.getMessage());
        }
    }

    private List<Prestamo> returnprestamos(Object object) {
        if (!(object instanceof List<?>)) {
            return null;
        }
        final List<?> list = (List<?>) object;
        for (Object item : list) {
            if (!(item instanceof Prestamo)) {
                return null;
            }
        }
        return (List<Prestamo>) list;
    }

    private void loadData() {
        final File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
                final Object object = objectInputStream.readObject();
                final List<Prestamo> list = returnprestamos(object);
                if (list == null) {
                    System.out.println("NO ES UNA LISTA DE PRESTAMOS");
                    return;
                }
                this.prestamos = list;
                System.out.println("SE CARGARON LOS DATOS CORRECTAMENTE");
            } catch (Exception e) {
                System.out.println("OCURRIO UN ERROR A LA HORA DE CARGAR DATOS: " + e.getMessage());
            }
        } else {
            System.out.println("EL ARCHIVO DE PERSISTENCIA NO EXISTE");
        }

    }


}
