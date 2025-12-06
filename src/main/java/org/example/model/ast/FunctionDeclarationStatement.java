package org.example.model.ast;

import org.example.model.Token;

import java.util.Objects;
import java.util.List;

public class FunctionDeclarationStatement extends Statement{
    private Token name;
    private List<Token> parameters;
    private List<Statement> body;

    public FunctionDeclarationStatement(Token name, List<Token> parameters, List<Statement> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public Token getName() {
        return name;
    }

    public List<Token> getParameters() {
        return parameters;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FunctionDeclarationStatement that = (FunctionDeclarationStatement) object;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
