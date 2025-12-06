package org.example.model.ast;


import java.util.List;
import java.util.Objects;

public class MatchExpression extends Expression {
    private final Expression value;
    private final List<CaseBranch> cases;
    private final Expression defaultCase;

    public MatchExpression(Expression value, List<CaseBranch> cases, Expression defaultCase) {
        this.value = value;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    public Expression getValue() {
        return value;
    }

    public List<CaseBranch> getCases() {
        return cases;
    }

    public Expression getDefaultCase() {
        return defaultCase;
    }

    @Override
    public String toString() {
        return "MatchExpression{" +
                "value=" + value +
                ", cases=" + cases +
                ", defaultCase=" + defaultCase +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MatchExpression that = (MatchExpression) object;
        return Objects.equals(value, that.value) && Objects.equals(cases, that.cases) && Objects.equals(defaultCase, that.defaultCase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, cases, defaultCase);
    }
}
