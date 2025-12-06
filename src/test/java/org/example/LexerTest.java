package org.example;

import org.example.exceptions.LexicalError;
import org.example.model.Token;
import org.example.model.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void tokenize() {
        String source = "3 + 2";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        List<Token> expected = List.of(
                new Token(TokenType.NUMBER, "3", 1),
                new Token(TokenType.PLUS, "+", 1),
                new Token(TokenType.NUMBER, "2", 1),
                new Token(TokenType.EOF, "", 1)
        );

        assertEquals(expected, tokens, "La liste des tokens  n'est pas conforme ");
    }


    @Test
    void testKeywords() {
        String source =  """
        x est 5;
        x devient 10;
        """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        List<Token> expected = List.of(
                new Token(TokenType.IDENTIFIER, "x", 1),
                new Token(TokenType.EQUAL, "est", 1),
                new Token(TokenType.NUMBER, "5", 1),
                new Token(TokenType.SEMICOLON,";",1),
                new Token(TokenType.IDENTIFIER,"x",2),
                new Token(TokenType.ASSIGN,"devient",2),
                new Token(TokenType.NUMBER, "10", 2),
                new Token(TokenType.SEMICOLON,";",2),
                new Token(TokenType.EOF,"",3)
        );

        assertEquals(expected, tokens, "Le lexer ne reconnaît pas correctement les mots clé");
    }

    @Test
    void testLexicalError(){
        String source = "3 @ 1";
        Lexer lexer = new Lexer(source);

        LexicalError exception = assertThrows(
                LexicalError.class,
                lexer::tokenize
        );

        String msg = exception.getPlainMessage();

        assertTrue(msg.contains("Caractère inattendu"));
        assertTrue(msg.contains("Ligne : 1"));
        assertTrue(msg.contains("@"));
    }
}