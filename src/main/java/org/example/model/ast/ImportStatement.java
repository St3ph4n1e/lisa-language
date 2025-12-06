package org.example.model.ast;

public class ImportStatement extends Statement {
    private final String path;

    public ImportStatement(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}