package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;

public class VariableAssignment extends Expression{
    private Token name;
    private Expression value;

    public VariableAssignment(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        VariableAssignment that = (VariableAssignment) object;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "VariableAssignment{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
