package org.example.Services;

import lombok.Getter;

public class pathArchivos {

    @Getter
    private String pathArchivo;

    public pathArchivos() {
        this.pathArchivo = getExecutionPath();

    }

    public String getExecutionPath() {
        try {
            String path = new java.io.File(
                    Biblioteca.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            ).getParent();

            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return ".";
        }
    }


}
