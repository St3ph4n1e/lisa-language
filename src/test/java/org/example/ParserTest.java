package org.example;

import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    /* Test User Story 7 */

    @Test
    void testSimpleAddition() {
        List<Token> tokens = List.of(
                new Token(TokenType.NUMBER, "3", 1),
                new Token(TokenType.PLUS, "+", 1),
                new Token(TokenType.NUMBER, "2", 1),
                new Token(TokenType.EOF, "", 1)
        );

        Parser parser = new Parser(tokens);
        Expression expr = parser.parseExpression();

        assertTrue(expr instanceof Binary, "L'expression devrait être de type Binary");
        Binary binary = (Binary) expr;

        assertTrue(binary.getLeft() instanceof Literal, "L'opérande gauche devrait être un Literal");
        assertEquals(3.0f, ((Literal) binary.getLeft()).getValue(), "La valeur gauche devrait être 3.0");

        assertEquals(TokenType.PLUS, binary.getOperator().getType(), "L'opérateur devrait être PLUS");

        assertTrue(binary.getRight() instanceof Literal, "L'opérande droite devrait être un Literal");
        assertEquals(2.0f, ((Literal) binary.getRight()).getValue(), "La valeur droite devrait être 2.0");
    }

    @Test
    void testOperatorPrecedence() {
        List<Token> tokens = List.of(
                new Token(TokenType.NUMBER, "3", 1),
                new Token(TokenType.PLUS, "+", 1),
                new Token(TokenType.NUMBER, "2", 1),
                new Token(TokenType.STAR, "*", 1),
                new Token(TokenType.NUMBER, "5", 1),
                new Token(TokenType.EOF, "", 1)
        );

        Parser parser = new Parser(tokens);
        Expression expr = parser.parseExpression();

        assertTrue(expr instanceof Binary, "L'expression racine devrait être de type Binary");
        Binary rootBinary = (Binary) expr;

        assertTrue(rootBinary.getLeft() instanceof Literal, "L'opérande gauche devrait être un Literal");
        assertEquals(3.0f, ((Literal) rootBinary.getLeft()).getValue(), "La valeur gauche devrait être 3.0");

        assertEquals(TokenType.PLUS, rootBinary.getOperator().getType(), "L'opérateur racine devrait être PLUS");

        assertTrue(rootBinary.getRight() instanceof Binary, "L'opérande droite devrait être une Binary (2 * 5)");
        Binary rightBinary = (Binary) rootBinary.getRight();

        assertEquals(2.0f, ((Literal) rightBinary.getLeft()).getValue(), "La multiplication gauche devrait être 2.0");
        assertEquals(TokenType.STAR, rightBinary.getOperator().getType(), "L'opérateur devrait être STAR");
        assertEquals(5.0f, ((Literal) rightBinary.getRight()).getValue(), "La multiplication droite devrait être 5.0");
    }

    @Test
    void testParenthesesGrouping() {
        List<Token> tokens = List.of(
                new Token(TokenType.LPAREN, "(", 1),
                new Token(TokenType.NUMBER, "1", 1),
                new Token(TokenType.PLUS, "+", 1),
                new Token(TokenType.NUMBER, "2", 1),
                new Token(TokenType.RPAREN, ")", 1),
                new Token(TokenType.STAR, "*", 1),
                new Token(TokenType.NUMBER, "3", 1),
                new Token(TokenType.EOF, "", 1)
        );

        Parser parser = new Parser(tokens);
        Expression expr = parser.parseExpression();

        assertTrue(expr instanceof Binary, "L'expression racine devrait être de type Binary");
        Binary rootBinary = (Binary) expr;

        assertEquals(TokenType.STAR, rootBinary.getOperator().getType(), "L'opérateur racine devrait être STAR");

        assertTrue(rootBinary.getLeft() instanceof Binary, "L'opérande gauche devrait être une Binary (1 + 2)");
        Binary leftBinary = (Binary) rootBinary.getLeft();

        assertEquals(1.0f, ((Literal) leftBinary.getLeft()).getValue(), "La valeur gauche du groupement devrait être 1.0");
        assertEquals(TokenType.PLUS, leftBinary.getOperator().getType(), "L'opérateur du groupement devrait être PLUS");
        assertEquals(2.0f, ((Literal) leftBinary.getRight()).getValue(), "La valeur droite du groupement devrait être 2.0");

        assertTrue(rootBinary.getRight() instanceof Literal, "L'opérande droite devrait être un Literal");
        assertEquals(3.0f, ((Literal) rootBinary.getRight()).getValue(), "La valeur droite devrait être 3.0");
    }
}

