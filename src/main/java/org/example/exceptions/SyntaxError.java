package org.example.exceptions;
import static org.example.utils.AnsiColors.*;

public class SyntaxError extends RuntimeException {

    private final Integer line;
    private final String offendingLiteral;

    public SyntaxError(String message, Integer line, String offendingLiteral) {
        super(message);
        this.line = line;
        this.offendingLiteral = offendingLiteral;
    }

    @Override
    public String getMessage(){
        if (line == null) {
            return """
%sErreur de syntaxe :%s %s%s%s
%sCause :%s %s'%s'%s
            """.formatted(
                    RED, RESET, BLUE, super.getMessage(), RESET,
                    RED, RESET, BLUE, offendingLiteral, RESET
            );
        }

        if (offendingLiteral == null) {
            return """
%sErreur de syntaxe :%s %s%s%s
%sLigne :%s %s%d%s
            """.formatted(
                    RED, RESET, BLUE, super.getMessage(), RESET,
                    RED, RESET, BLUE, line, RESET
            );
        }
        return """
%sErreur de syntaxe :%s %s%s%s
%sLigne :%s %s%d%s
%sÀ côté de :%s %s'%s'%s
                """.formatted(
                RED, RESET, BLUE, super.getMessage(), RESET,
                RED, RESET, BLUE, line, RESET,
                RED, RESET, BLUE, offendingLiteral, RESET
        );
    }
}
