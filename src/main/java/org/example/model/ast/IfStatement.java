package org.example.model.ast;

import java.util.Objects;
import java.util.Optional;

public class IfStatement extends Statement {
    private Expression condition;
    private Statement thenBranch;
    private Optional<Statement> elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Optional<Statement> elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public void setThenBranch(Statement thenBranch) {
        this.thenBranch = thenBranch;
    }

    public void setElseBranch(Optional<Statement> elseBranch) {
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public Optional<Statement> getElseBranch() {
        return elseBranch;
    }

    public Statement getThenBranch() {
        return thenBranch;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IfStatement that = (IfStatement) object;
        return Objects.equals(condition, that.condition) && Objects.equals(thenBranch, that.thenBranch) && Objects.equals(elseBranch, that.elseBranch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, thenBranch, elseBranch);
    }

    @Override
    public String toString() {
        return "IfStatement{" +
                "condition=" + condition +
                ", thenBranch=" + thenBranch +
                ", elseBranch=" + elseBranch +
                '}';
    }
}
