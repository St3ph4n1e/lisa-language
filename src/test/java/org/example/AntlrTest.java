package org.example;

import org.example.antlr.Parser;
import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AntlrTest {

    @Test
    void itShouldParse_var_declaration() {
        String source = " variable a est 1 ;";
        List<Statement> ast = new Parser(source).parse();
        assertEquals(ast, List.of(
                new VarStatement(
                        new Token(TokenType.IDENTIFIER, "a", 1),
                        new Literal(1.0f))
        ));
    }

    @Test
    void itShouldParse_var_assignment() {
        String source = " variable a est 1 ;\n a est 2 ;";
        List<Statement> ast = new Parser(source).parse();
        assertEquals(ast, List.of(
                new VarStatement(
                        new Token(TokenType.IDENTIFIER, "a", 1),
                        new Literal(1.0f)),
                new ExpressionStatement(
                        new VariableAssignment(
                                new Token(TokenType.IDENTIFIER, "a", 2),
                                new Literal(2.0f)
                        )
                )
        ));
    }

    @Test
    void itShouldParse_block_declaration() {
        String source = "debut variable a est 1 ; fin";
        List<Statement> ast = new Parser(source).parse();
        VarStatement varStatement = new VarStatement(new Token(TokenType.IDENTIFIER, "a", 1), new Literal(1.0f));
        List<Statement> statementsExpected= List.of(varStatement);
        assertEquals(ast, List.of(
                new BlockStatement(statementsExpected)
        ));
    }

    @Test
    void itShouldParse_affiche_declaration() {
        String source = "affiche 1 + 2 ;";
        List<Statement> ast = new Parser(source).parse();
        assertEquals(ast, List.of(
                new PrintStatement(
                        new Binary(
                                new Literal(1.0f),
                                new Token(TokenType.PLUS, "+", 1),
                                new Literal(2.0f)
                        )
                )
        ));
    }

    @Test
    void itShouldParse_if_assignment() {
        String source = " si ( 1 egale 2 ) debut \n" +
                "a est 2 ;\n" +
                "fin sinon debut \n" +
                "affiche 4 ; \n" +
                "fin";
        List<Statement> ast = new Parser(source).parse();

        ExpressionStatement then_expressionStatement = new ExpressionStatement(
                new VariableAssignment(
                        new Token(TokenType.IDENTIFIER, "a", 2),
                        new Literal(2.0f)
                )
        );
        List<Statement> statementsExpected_then= List.of(then_expressionStatement);

        PrintStatement printStatement_else = new PrintStatement( new Literal(4.0f));
        List<Statement> statementsExpected_else= List.of(printStatement_else);
        assertEquals(ast, List.of(
                new IfStatement(
                        new Binary(
                                new Literal(1.0f),
                                new Token(TokenType.EQUAL_EQUAL, "egale", 1),
                                new Literal(2.0f)
                        ),
                        new BlockStatement( statementsExpected_then),
                        Optional.of(new BlockStatement(statementsExpected_else)
                        )
                )
        ));
    }

    @Test
    void itShouldParse_while_loop() {
        String source =
                "variable i est 5; \n" +
                        "tant_que ( i plus_petit_que 5 ) debut\n" +
                        "affiche i; \n" +
                        "i est i + 1; \n" +
                        "fin";

        List<Statement> ast = new Parser(source).parse();

        VarStatement variableAssignment = new VarStatement(
                new Token(TokenType.IDENTIFIER, "i", 1),
                new Literal(5.0f)
        );
        Expression condition = new Binary(
                new Variable(new Token(TokenType.IDENTIFIER, "i", 2)),
                new Token(TokenType.LESS, "plus_petit_que", 2),
                new Literal(5.0f)
        );
        PrintStatement printI = new PrintStatement(
                new Variable(new Token(TokenType.IDENTIFIER, "i", 3))
        );
        ExpressionStatement increment = new ExpressionStatement(
                new VariableAssignment(
                        new Token(TokenType.IDENTIFIER, "i", 4),
                        new Binary(
                                new Variable(new Token(TokenType.IDENTIFIER, "i", 4)),
                                new Token(TokenType.PLUS, "+", 4),
                                new Literal(1.0f)
                        )
                )
        );
        List<Statement> whileStatements = List.of(printI, increment);

        WhileStatement whileStatement = new WhileStatement(
                condition,
                new BlockStatement(whileStatements)
        );
        assertEquals(List.of(variableAssignment, whileStatement), ast);
    }

    @Test
    void itShouldParse_while_TEST() {
        String source =
                "tant_que ( 2 plus_petit_que 5 ) debut\n" +
                        "affiche 2; \n" +
                        "fin";

        List<Statement> ast = new Parser(source).parse();

        Expression condition = new Binary(
                new Literal(2.0f),
                new Token(TokenType.LESS, "plus_petit_que", 1),
                new Literal(5.0f)
        );

        PrintStatement printI = new PrintStatement(
                new Literal(2.0f)
        );

        List<Statement> whileStatements = List.of(printI);

        WhileStatement whileStatement = new WhileStatement(
                condition,
                new BlockStatement(whileStatements)
        );

        assertEquals(List.of( whileStatement), ast);
    }

    @Test
    void itShouldParse_return() {
        String source = " si ( 1 egale 2 ) debut \n" +
                "a est 2 ;\n" +
                "fin sinon debut \n" +
                "retourne 4; \n" +
                "fin";
        List<Statement> ast = new Parser(source).parse();
        ExpressionStatement then_expressionStatement = new ExpressionStatement (
                new VariableAssignment(
                        new Token(TokenType.IDENTIFIER, "a", 2),
                        new Literal(2.0f)
                )
        );
        List<Statement> statementsExpected_then= List.of(then_expressionStatement);

        ReturnStatement printStatement_else = new ReturnStatement( new Token(TokenType.RETURN, "retourne", 4), Optional.of(new Literal(4.0f)));
        List<Statement> statementsExpected_else= List.of(printStatement_else);
        assertEquals(ast, List.of(
                new IfStatement(
                        new Binary(
                                new Literal(1.0f),
                                new Token(TokenType.EQUAL_EQUAL, "egale", 1),
                                new Literal(2.0f)
                        ),
                        new BlockStatement( statementsExpected_then),
                        Optional.of(new BlockStatement(statementsExpected_else)
                        )
                )
        ));
    }
}
