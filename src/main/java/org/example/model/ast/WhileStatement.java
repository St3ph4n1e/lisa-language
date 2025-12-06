package org.example.model.ast;

import java.util.Objects;

public class WhileStatement extends Statement{
    private Expression condition;
    private Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        WhileStatement that = (WhileStatement) object;
        return Objects.equals(condition, that.condition) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, body);
    }

    @Override
    public String toString() {
        return "WhileStatement{" +
                "condition=" + condition +
                ", body=" + body +
                '}';
    }
}
