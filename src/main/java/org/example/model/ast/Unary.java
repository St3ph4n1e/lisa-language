package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;

public class Unary extends Expression {
    private Token operator;
    private Expression right;

    public Unary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Unary unary = (Unary) object;
        return Objects.equals(operator, unary.operator) && Objects.equals(right, unary.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, right);
    }
}
