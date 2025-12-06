package org.example;

import org.example.exceptions.RuntimeError;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        if (values.containsKey(name)) {
            throw new RuntimeError(
                    "Cette variable est déjà définie",
                    null,
                    name
            );
        }
        values.put(name, value);
    }

    public Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        } else if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(
                "Cette variable n'existe pas",
                null,
                name
        );
    }

    public void assign(String name, Object value) {
        if (values.containsKey(name)) {
            values.put(name, value);
        } else if (enclosing != null) {
            enclosing.assign(name, value);
        } else {
            throw new RuntimeError(
                    "Cette variable n'est pas définie",
                    null,
                    name
            );
        }
    }
}
