package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;

public class Binary extends Expression {
    private Expression left ;
    private Token operator ;
    private Expression right;

    public Binary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
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
        Binary binary = (Binary) object;
        return Objects.equals(left, binary.left) && Objects.equals(operator, binary.operator) && Objects.equals(right, binary.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, operator, right);
    }

    @Override
    public String toString() {
        return "Binary{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }
}
