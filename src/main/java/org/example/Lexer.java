package org.example;

import org.example.exceptions.LexicalError;
import org.example.exceptions.SyntaxError;
import org.example.model.Token;
import org.example.model.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String source;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize(){
        while (!isAtEnd() ){
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "",line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();

        switch (c) {
            case '+' -> addToken(TokenType.PLUS,null);
            case '-' -> {
                if (peek() == '>') {
                    advance();
                    addToken(TokenType.ARROW, null);
                } else {
                    addToken(TokenType.MINUS, null);
                }
            }
            case '*' -> addToken(TokenType.STAR,null);
            case '/' -> addToken(TokenType.SLASH,null);
            case '(' -> addToken(TokenType.LPAREN,null);
            case ')' -> addToken(TokenType.RPAREN,null);
            case '{' -> addToken(TokenType.LBRACE, null);
            case '}' -> addToken(TokenType.RBRACE, null);
            case ',' -> addToken(TokenType.COMMA, null);
            case ';' -> addToken(TokenType.SEMICOLON,null);
            case '"' -> string();
            case ' ', '\r', '\t' -> {}
            case '\n' -> line++;

            default -> {
                if (Character.isDigit(c)) {
                    number();
                } else if (Character.isLetter(c)) {
                    identifier();
                } else {
                    throw new LexicalError(
                            "Caractère inattendu",
                            line,
                            String.valueOf(c)
                    );
                }
            }
        }
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()){
            if (peek() == '\n') {
                ++line;
            }
            advance();
        }
        if (isAtEnd()) {
            throw new SyntaxError(
                    "Chaine de caractère non déterminée",
                    line,
                    String.valueOf(peek())
            );
        }

        advance(); // handle error here
        addToken(TokenType.STRING, source.substring(start + 1 , current - 1));
    }
    private void identifier() {
        while (isAlphanumeric(peek()) || peek() == '_') {
            advance();
        }
        String text = source.substring(start, current).toLowerCase();
        TokenType type = switch (text) {
            case "correspond_a" -> TokenType.MATCH;
            case "par_defaut" -> TokenType.DEFAULT;
            default -> TokenType.IDENTIFIER;
        };
        TokenType tokenType = Keywords.KEYWORDS.getOrDefault(text, type);
        addToken(tokenType, null);
    }

    private boolean isAlphanumeric(char c) {
        return Character.isLetter(c) || Character.isDigit(c);
    }


    private void number() {
        while (Character.isDigit(peek())){
            advance();
        }
        if (peek() == '.' && Character.isDigit(peekNext())) {
            advance();
            while (Character.isDigit(peek())){
                advance();
            }
        }
        String value = source.substring(start, current);
        addToken(TokenType.NUMBER, value);

    }



    private char advance() {
        current += 1;
        return source.charAt(current - 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType tokenType, String literal ) {
        String text = source.substring(start,current);
        String lexeme = (literal != null) ? literal : text;
        tokens.add(new Token(tokenType,lexeme,line));
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()){
            return '\0' ;
        }
        return source.charAt(current + 1);
    }
}
