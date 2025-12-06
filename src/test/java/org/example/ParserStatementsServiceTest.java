package org.example;

import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.example.service.ParserStatementsService;
import org.example.utils.TokenReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParserStatementsServiceTest {
    @Test
    void itShouldParse_var_declaration() {
        // given
        String source = " variable a est 1 ;";
        ParserStatementsService parserStatementsService = initializerTest(source);
        // when
        List<Statement> ast = parserStatementsService.parse();

        // then
        assertEquals(ast, List.of(
                new VarStatement(
                        new Token(TokenType.IDENTIFIER, "a", 1),
                        new Literal(1.0f))
        ));
    }
    @Test
    void itShouldParse_var_assignment() {
        // given
        String source = " variable a est 1 ;\n a est 2 ;";
        ParserStatementsService parserStatementsService = initializerTest(source);
        // when
        List<Statement> ast = parserStatementsService.parse();

        // then
        assertEquals(ast, List.of(
                new VarStatement(
                        new Token(TokenType.IDENTIFIER, "a", 1),
                        new Literal(1.0f)),
                new ExpressionStatement (
                        new VariableAssignment(
                                new Token(TokenType.IDENTIFIER, "a", 2),
                                new Literal(2.0f)
                        )
                )
        ));

    }
    @Test
    void itShouldParse_block_declaration() {
        // given
        String source = "debut variable a est 1 ; fin";
        ParserStatementsService parserStatementsService = initializerTest(source);
        // when
        List<Statement> ast = parserStatementsService.parse();
        VarStatement varStatement = new VarStatement(new Token(TokenType.IDENTIFIER, "a", 1), new Literal(1.0f));
        List<Statement> statementsExpected= List.of(varStatement);
        // then
        assertEquals(ast, List.of(
                new BlockStatement(statementsExpected)
        ));

    }
    @Test
    void itShouldParse_affiche_declaration() {
        // given
        String source = "affiche 1 + 2 ;";
        ParserStatementsService parserStatementsService = initializerTest(source);
        // when
        List<Statement> ast = parserStatementsService.parse();
        // then
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
        // given
        String source = " si ( 1 egale 2 ) debut \n" +
                "a est 2 ;\n" +
                "fin sinon debut \n" +
                "affiche 4 ; \n" +
                "fin";
        ParserStatementsService parserStatementsService = initializerTest(source);
        // when
        List<Statement> ast = parserStatementsService.parse();
        ExpressionStatement then_expressionStatement = new ExpressionStatement (
                new VariableAssignment(
                        new Token(TokenType.IDENTIFIER, "a", 2),
                        new Literal(2.0f)
                )
        );
        List<Statement> statementsExpected_then= List.of(then_expressionStatement);

        PrintStatement printStatement_else = new PrintStatement( new Literal(4.0f));
        List<Statement> statementsExpected_else= List.of(printStatement_else);
        // then
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
        // given
        String source =
                "variable i est 5; \n" +
                        "tant_que ( i plus_petit_que 5 ) debut\n" +
                        "affiche i; \n" +
                        "i est i + 1; \n" +
                        "fin";

        ParserStatementsService parserStatementsService = initializerTest(source);

        // when
        List<Statement> ast = parserStatementsService.parse();

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
        // then
        assertEquals(List.of(variableAssignment, whileStatement), ast);
    }

    @Test
    void itShouldParse_while_TEST() {
        // given
        String source =
                        "tant_que ( 2 plus_petit_que 5 ) debut\n" +
                        "affiche 2; \n" +
                        "fin";

        ParserStatementsService parserStatementsService = initializerTest(source);

        // when
        List<Statement> ast = parserStatementsService.parse();

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

        // then
        assertEquals(List.of( whileStatement), ast);
    }
    @Test
    void itShouldParse_return() {
        // given
        String source = " si ( 1 egale 2 ) debut \n" +
                "a est 2 ;\n" +
                "fin sinon debut \n" +
                "retourne 4; \n" +
                "fin";
        ParserStatementsService parserStatementsService = initializerTest(source);
        // when
        List<Statement> ast = parserStatementsService.parse();
        ExpressionStatement then_expressionStatement = new ExpressionStatement (
                new VariableAssignment(
                        new Token(TokenType.IDENTIFIER, "a", 2),
                        new Literal(2.0f)
                )
        );
        List<Statement> statementsExpected_then= List.of(then_expressionStatement);

        ReturnStatement printStatement_else = new ReturnStatement( new Token(TokenType.RETURN, "retourne", 4), Optional.of(new Literal(4.0f)));
        List<Statement> statementsExpected_else= List.of(printStatement_else);
        // then
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
    void itShouldParse_match_expression() {

        String source =
                "variable a est correspond_a x debut \n" +
                        "    \"vale\" -> \"un\",\n" +
                        "    2 -> \"deux\",\n" +
                        "    par_defaut -> \"autre\",\n" +
                        "fin;";

        ParserStatementsService parserStatementsService = initializerTest(source);

        List<Statement> ast = parserStatementsService.parse();

        Expression matchValue = new Variable(
                new Token(TokenType.IDENTIFIER, "x", 1)
        );

        CaseBranch case1 = new CaseBranch(
                new Literal("vale"),
                new Literal("un")
        );

        CaseBranch case2 = new CaseBranch(
                new Literal(2.0f),
                new Literal("deux")
        );

        Expression defaultCase = new Literal("autre");

        MatchExpression matchExpr = new MatchExpression(
                matchValue,
                List.of(case1, case2),
                defaultCase
        );

        VarStatement expectedVar = new VarStatement(
                new Token(TokenType.IDENTIFIER, "a", 1),
                matchExpr
        );

        System.out.println(List.of(expectedVar));
        System.out.println(ast);

        assertEquals(List.of(expectedVar), ast);
    }
    private ParserStatementsService initializerTest(String source){
        Lexer lexer = new Lexer(source);
        List<Token> tokensResult = lexer.tokenize();
        Parser parser = new Parser(tokensResult);
        TokenReader tokenReader = new TokenReader(parser);
        return new ParserStatementsService(parser, tokenReader);
    }

    @Test
    void itShouldSkipInvalidStatementAndContinueParsing() {
        // given : une instruction valide, une invalide, une valide
        String source =
                "affiche 1;\n" +
                        "affiche ;\n" +
                        "affiche 3;";

        ParserStatementsService parserStatementsService = initializerTest(source);

        // when
        List<Statement> ast = parserStatementsService.parse();

        // then : on doit avoir 2 instructions (la 2ᵉ est ignorée à cause de l'erreur)
        assertEquals(2, ast.size(), "Le parser doit continuer après une erreur de syntaxe");

        // 1ère instruction : affiche 1;
        Statement first = ast.get(0);
        assertTrue(first instanceof PrintStatement);
        Expression firstExpr = ((PrintStatement) first).getExpression();
        assertTrue(firstExpr instanceof Literal);
        assertEquals(1.0f, ((Literal) firstExpr).getValue());

        // 2ᵉ instruction : affiche 3;
        Statement second = ast.get(1);
        assertTrue(second instanceof PrintStatement);
        Expression secondExpr = ((PrintStatement) second).getExpression();
        assertTrue(secondExpr instanceof Literal);
        assertEquals(3.0f, ((Literal) secondExpr).getValue());
    }
}
