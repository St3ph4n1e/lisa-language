package org.example.exceptions;
import static org.example.utils.AnsiColors.*;

public class LexicalError extends RuntimeException {

    private final int line;
    private final String offendingChar;
    //Handle errors from Lexer
    public LexicalError(String message, int line, String offendingChar) {

        super(message);
        this.line = line;
        this.offendingChar = offendingChar;
    }

    @Override
    public String getMessage(){
        return """
                %s%sErreur lexicale :%s %s%s%s
                %s%sLigne :%s %s%d%s
                %s%sCaractère :%s %s'%s'%s
                """.formatted(
                    RED, BOLD, RESET, BLUE, super.getMessage(), RESET,

                    RED, BOLD, RESET, BLUE, line, RESET,

                    RED, BOLD, RESET, BLUE, offendingChar, RESET
            );
    }
    //Method for test beacause of ansi colors
    public String getPlainMessage() {
        return "Erreur lexicale : " + super.getMessage()
                + "Ligne : " + line
                + "Caractère : " + offendingChar ;
    }
}
