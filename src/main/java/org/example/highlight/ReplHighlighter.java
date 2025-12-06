package org.example.highlight;

import org.example.Lexer;
import org.example.exceptions.LexicalError;
import org.example.model.Token;
import org.example.model.TokenType;

import java.util.List;

import static org.example.utils.AnsiColors.*;

public class ReplHighlighter {

    public static String highlight(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens;

        try {
            tokens = lexer.tokenize();
        } catch (LexicalError e) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {

            if (t.getType() == TokenType.EOF) {
                continue;
            }

            String lexeme = t.getLexeme();

            switch (t.getType()) {
                case VAR, IF, ELSE, WHILE, PRINT, RETURN,
                     FN, TRUE, FALSE, NULL, AND, OR, THEN -> sb.append(BRIGHT_BLUE).append(BOLD).append(lexeme).append(RESET).append(" ");

                case GREATER, GREATER_EQUAL,
                     LESS, LESS_EQUAL,
                     EQUAL, EQUAL_EQUAL,
                     BANG, BANG_EQUAL -> sb.append(BRIGHT_CYAN).append(lexeme).append(RESET).append(" ");
                case LPAREN, RPAREN -> sb.append(BLUE ).append(lexeme).append(RESET).append(" ");
                case LBRACE-> sb.append(BRIGHT_PURPLE).append(lexeme).append(RESET).append("\n").append("\t");
                case RBRACE-> sb.append(BRIGHT_PURPLE).append(lexeme).append(RESET);
                case IDENTIFIER -> sb.append(WHITE).append(lexeme).append(RESET).append(" ");
                case NUMBER -> sb.append(BRIGHT_YELLOW).append(lexeme).append(RESET).append(" ");
                case STRING -> sb.append(BRIGHT_GREEN).append("\"").append(lexeme).append("\"").append(RESET).append(" ");
                case SEMICOLON -> sb.append(YELLOW).append(lexeme).append(RESET).append("\n");
                default -> sb.append(lexeme).append(" ");

            }
        }
        return sb.toString();
    }
}