package org.example.model.ast;

import java.util.List;
import java.util.Objects;

public class BlockStatement extends Statement {
    private List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BlockStatement that = (BlockStatement) object;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }

    @Override
    public String toString() {
        return "BlockStatement{" +
                "statements=" + statements +
                '}';
    }
}
