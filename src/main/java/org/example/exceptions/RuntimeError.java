package org.example.exceptions;

import static org.example.utils.AnsiColors.*;

public class RuntimeError extends RuntimeException {
    private final Integer line;
    private final String offendingElement;
    public RuntimeError(String message, Integer line, String offendingElement) {

        super(message);
        this.line = line;
        this.offendingElement = offendingElement;
    }

    @Override
    public String getMessage(){
        if (line == null) {
            return """
%sErreur d'exécution :%s %s%s%s
%sCause :%s %s'%s'%s
            """.formatted(
                    RED, RESET, BLUE, super.getMessage(), RESET,
                    RED, RESET, BLUE, offendingElement, RESET
            );
        }

        return """
%sErreur d'exécution :%s %s%s%s
%sLigne :%s %s%d%s
%sCause :%s %s'%s'%s
                """.formatted(
                RED, RESET, BLUE, super.getMessage(), RESET,
                RED, RESET, BLUE, line, RESET,
                RED, RESET, BLUE, offendingElement, RESET
        );
    }
}
