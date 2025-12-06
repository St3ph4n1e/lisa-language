package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;

public class VarStatement extends Statement{
    private Token name;
    private Expression initializer;

    public VarStatement(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    public Token getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        VarStatement that = (VarStatement) object;
        return Objects.equals(name, that.name) && Objects.equals(initializer, that.initializer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, initializer);
    }

    @Override
    public String toString() {
        return "VarStatement{" +
                "name=" + name +
                ", initializer=" + initializer +
                '}';
    }
}
