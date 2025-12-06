package org.example;
import org.example.model.Token;
import org.example.model.ast.*;
import org.example.service.ParserStatementsService;
import org.example.utils.TokenReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private List<Statement> parseWithCustom(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        TokenReader tokenReader = new TokenReader(parser);
        ParserStatementsService parserStatementsService = new ParserStatementsService(parser, tokenReader);
        return parserStatementsService.parse();
    }

    private String getOutput() {
        return outputStream.toString();
    }

    @Test
    void testIfElseWithAge() {
        String source = "variable age est 17; si (age plus_grand_que 17) debut affiche \"Vous êtes majeur !\"; fin sinon debut affiche \"Vous etes mineur !\"; fin";
        List<Statement> ast = parseWithCustom(source);
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast, 0);
        assertEquals("Vous etes mineur !\n", getOutput());
    }

    @Test
    void testWhileLoopWithDevient() {
        String source = "variable a est 0; tant_que (a plus_petit_que 5) debut affiche a; a devient a + 1; fin";
        List<Statement> ast = parseWithCustom(source);
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast, 0);
        assertEquals("0.0\n1.0\n2.0\n3.0\n4.0\n", getOutput());
    }

    @Test
    void testFunctionWithTwoParameters() {
        String source = "fonction ajouter(a et b) debut variable resultat est a + b; retourne resultat; fin variable mon_calcul est ajouter(1 et 2); affiche mon_calcul;";
        List<Statement> ast = parseWithCustom(source);
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast, 0);
        assertEquals("3.0\n", getOutput());
    }

    @Test
    void testFunctionWithoutReturn() {
        String source = "fonction afficher_message() debut affiche \"Message\"; fin variable resultat est afficher_message(); affiche resultat;";
        List<Statement> ast = parseWithCustom(source);
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast, 0);
        assertEquals("Message\nnull\n", getOutput());
    }

    @Test
    void testFunctionWithThreeParameters() {
        String source = "fonction multiplier(a et b et c) debut retourne a * b * c; fin variable resultat est multiplier(14 et 3 et 6); affiche resultat;";
        List<Statement> ast = parseWithCustom(source);
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast, 0);
        assertEquals("252.0\n", getOutput());
    }
}

/*

Tests REPL :

variable age est 17; si (age plus_grand_que  17) debut affiche "Vous êtes majeur !"; fin sinon debut affiche "Vous etes mineur !"; fin

variable a est 0; tant_que (a plus_petit_que 5) debut affiche a; a devient a + 1; fin

fonction ajouter(a et b) debut variable resultat est a + b; retourne resultat; fin variable mon_calcul est ajouter(1 et 2); affiche mon_calcul;

fonction afficher_message() debut affiche "Message"; fin variable resultat est afficher_message(); affiche resultat;

fonction multiplier(a et b et c) debut retourne a * b * c; fin variable resultat est multiplier(14 et 3 et 6); affiche resultat;


*/