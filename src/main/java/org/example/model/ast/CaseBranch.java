package org.example.model.ast;

import java.util.Objects;

public class CaseBranch {
    public final Expression pattern;
    public final Expression body;

    public CaseBranch(Expression pattern, Expression body) {
        this.pattern = pattern;
        this.body = body;
    }

    public Expression getPattern() {
        return pattern;
    }

    public Expression getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "CaseBranch{" +
                "pattern=" + pattern +
                ", body=" + body +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CaseBranch that = (CaseBranch) object;
        return Objects.equals(pattern, that.pattern) && Objects.equals(body, that.body);
    }
    @Override
    public int hashCode() {
        return Objects.hash(pattern, body);
    }
}
