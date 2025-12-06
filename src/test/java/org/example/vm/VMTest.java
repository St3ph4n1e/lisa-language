package org.example.vm;

import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VMTest {
    @Test
    void itShouldRunCompiledPrintExpression() {
        // given :  `affiche 1 + 2 * 3;`
        Expression expr = new Binary(
                new Literal(1f),
                new Token(TokenType.PLUS, "+", 1),
                new Binary(
                        new Literal(2f),
                        new Token(TokenType.STAR, "*", 1),
                        new Literal(3f)
                )
        );
        Statement print = new PrintStatement(expr);
        List<Statement> ast = List.of(print);

        // Compilation : AST -> Chunk (bytecode)
        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(ast);

        // On catch System.out to check
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // when :
            VM vm = new VM();
            Object result = vm.run(chunk);

            // The stack is empty by PRINT, so RETURN returns null
            assertNull(result, "La VM ne doit pas retourner de valeur dans ce cas");
        } finally {
            // We restore the standard output
            System.setOut(originalOut);
        }

        // then : Verify that the VM has correctly displayed 7.0
        String output = outContent.toString().trim();
        assertEquals("7.0", output, "La VM doit afficher '7.0' pour 'affiche 1 + 2 * 3;'");
    }

    @Test
    void itShouldCompareValues() {
        // AST : print(1 < 2);
        Expression expr = new Binary(
                new Literal(1.0),
                new Token(TokenType.LESS, "plus_petit_que", 1),
                new Literal(2.0)
        );

        Statement stmt = new PrintStatement(expr);
        List<Statement> program = List.of(stmt);

        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(program);

        VM vm = new VM();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        vm.run(chunk);

        System.setOut(original);

        assertEquals("true\n", out.toString());
    }

    @Test
    void itShouldCompareEquality() {
        Expression expr = new Binary(
                new Literal(3.0),
                new Token(TokenType.EQUAL_EQUAL, "surement_est", 1),
                new Literal(3.0)
        );

        Statement stmt = new PrintStatement(expr);
        List<Statement> program = List.of(stmt);

        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(program);

        VM vm = new VM();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        vm.run(chunk);
        System.setOut(System.out);

        assertEquals("true\n", out.toString());
    }

    @Test
    void itShouldExecuteIfInVM() {
        // condition : 1 < 2
        Expression condition = new Binary(
                new Literal(1.0),
                new Token(TokenType.LESS, "plus_petit_que", 1),
                new Literal(2.0)
        );

        // then : affiche 42;
        Statement thenBranch = new PrintStatement(new Literal(42.0));

        // If sans else
        IfStatement ifStmt = new IfStatement(condition, thenBranch, Optional.empty());

        List<Statement> program = List.of(ifStmt);

        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(program);

        VM vm = new VM();

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            vm.run(chunk);
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("42.0\n", outContent.toString());
    }

    @Test
    void itShouldExecuteIfElseInVM() {
        // si (2 plus_petit_que 1) { affiche 1; } sinon { affiche 2; }

        // condition : 2 < 1
        Expression condition = new Binary(
                new Literal(2.0),
                new Token(TokenType.LESS, "plus_petit_que", 1),
                new Literal(1.0)
        );

        // then branch : affiche 1;
        Statement thenBranch = new PrintStatement(
                new Literal(1.0)
        );

        // else branch : affiche 2;
        Statement elseBranch = new PrintStatement(
                new Literal(2.0)
        );

        IfStatement ifStmt = new IfStatement(
                condition,
                thenBranch,
                Optional.of(elseBranch)
        );

        List<Statement> program = List.of(ifStmt);

        // Compile Lisa AST -> bytecode
        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(program);

        VM vm = new VM();

        // On capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            vm.run(chunk);
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("2.0\n", outContent.toString());
    }

    @Test
    void itShouldSkipWhileBodyWhenConditionIsFalse() {
        // tant_que (false) { affiche 1; }

        // condition : false
        Expression condition = new Literal(false);

        // body : affiche 1;
        Statement body = new PrintStatement(new Literal(1.0));

        WhileStatement whileStmt = new WhileStatement(condition, body);

        List<Statement> program = List.of(whileStmt);

        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(program);

        VM vm = new VM();

        // Capture de la sortie
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            vm.run(chunk);
        } finally {
            System.setOut(originalOut);
        }

        // La condition est fausse dès le début -> le body ne doit jamais être exécuté
        assertEquals("", outContent.toString());
    }

    @Test
    void itShouldHandleGlobalVariableInVM() {
        // variable x est 1;
        VarStatement varX = new VarStatement(
                new Token(TokenType.IDENTIFIER, "x", 1),
                new Literal(1.0)
        );

        // affiche x;
        PrintStatement printX = new PrintStatement(
                new Variable(new Token(TokenType.IDENTIFIER, "x", 1))
        );

        List<Statement> program = List.of(varX, printX);

        LisaCompiler compiler = new LisaCompiler();
        Chunk chunk = compiler.compile(program);

        VM vm = new VM();

        // Capture System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            vm.run(chunk);
        } finally {
            System.setOut(originalOut);
        }

        // Les nombres sont des doubles -> "1.0\n"
        assertEquals("1.0\n", outContent.toString());
    }
}