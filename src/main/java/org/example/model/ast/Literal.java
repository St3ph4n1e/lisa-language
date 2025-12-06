package org.example.model.ast;

import java.util.Objects;

public class Literal extends Expression{
    private Object value;



    public Literal(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Literal literal = (Literal) object;
        return Objects.equals(value, literal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value=" + value +
                '}';
    }
}
