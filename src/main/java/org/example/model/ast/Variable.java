package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;

public class Variable extends  Expression{
    private Token name ;

    public Variable(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Variable variable = (Variable) object;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public String toString() {
        return "Variable{" +
                "name=" + name +
                '}';
    }
}
