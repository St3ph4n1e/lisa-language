package org.example;

import org.example.exceptions.ReturnException;
import org.example.exceptions.RuntimeError;
import org.example.model.Token;
import org.example.model.ast.FunctionDeclarationStatement;

import java.util.List;

public class LisaFunction {
    private final Interpreter interpreter;
    private final FunctionDeclarationStatement declaration;
    private final Environment closure;

    public LisaFunction(Interpreter interpreter, FunctionDeclarationStatement declaration, Environment closure) {
        this.interpreter = interpreter;
        this.declaration = declaration;
        this.closure = closure;
    }

    public Object call(List<Object> arguments) {
        if (arguments.size() != declaration.getParameters().size()) {
            throw new RuntimeError(
                    "Nombre d'arguments incorrect pour " + declaration.getName().getLexeme(),
                    null,
                    "Attendu: " + declaration.getParameters().size() + ", Reçu: " + arguments.size()
            );
        }

        Environment functionEnv = new Environment(this.closure);

        for (int i = 0; i < declaration.getParameters().size(); i++) {
            Token param = declaration.getParameters().get(i);
            Object arg = arguments.get(i);
            functionEnv.define(param.getLexeme(), arg);
        }

        try {
            this.interpreter.executeBlock(this.declaration.getBody(), functionEnv);
            return null;
        } catch (ReturnException returnValue) {
            return returnValue.getValue();
        }
    }
}