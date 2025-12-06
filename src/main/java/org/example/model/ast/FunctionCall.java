package org.example.model.ast;

import java.util.List;
import java.util.Objects;

public class FunctionCall extends Expression {
    private Expression callee;
    private List<Expression> arguments;

    public FunctionCall(Expression callee, List<Expression> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    public Expression getCallee() {
        return callee;
    }

    public void setCallee(Expression callee) {
        this.callee = callee;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public void setArguments(List<Expression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FunctionCall that = (FunctionCall) o;
        return Objects.equals(callee, that.callee) && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callee, arguments);
    }

}




