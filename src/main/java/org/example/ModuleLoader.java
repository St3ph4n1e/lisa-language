package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModuleLoader {

    public static String loadModule(String currentFilePath, String importPath) throws IOException {
        Path path;
        if (importPath.startsWith("/")) {
            // Chemin absolu
            path = Paths.get(importPath);
        } else {
            // Chemin relatif
            Path currentDir = Paths.get(currentFilePath).getParent();
            if (currentDir == null) {
                currentDir = Paths.get(".");
            }
            path = currentDir.resolve(importPath).normalize();
        }
        return Files.readString(path);
    }
}
