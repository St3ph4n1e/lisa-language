package org.example.vm;

import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LisaCompilerTest {

    @Test
    void compile() {
    }

    @Test
    void itShouldCompilePrintLiteral() {
        // given : AST equal to à `affiche 1;`
        Statement print = new PrintStatement(new Literal(1f));
        List<Statement> ast = List.of(print);

        LisaCompiler compiler = new LisaCompiler();

        // when : we compile AST in bytecode (Chunk)
        Chunk chunk = compiler.compile(ast);

        // then : we check the constant List
        List<Object> constants = chunk.getConstants();
        assertEquals(1, constants.size(), "On doit avoir exactement 1 constante");
        assertEquals(1f, (Float) constants.get(0));

        // then : we check the code
        List<Integer> code = chunk.getCode();

        // We expect :
        // CONSTANT 0
        // PRINT
        // RETURN
        List<Integer> expectedCode = List.of(
                OpCode.CONSTANT.ordinal(), // push constants[0]
                0,                         // index 0
                OpCode.PRINT.ordinal(),    // print top of stack
                OpCode.RETURN.ordinal()    // fin du programme
        );

        assertEquals(expectedCode, code, "Le bytecode généré n'est pas celui attendu");
    }

    @Test
    void itShouldCompilePrintBinaryExpression() {
        // given : AST  `affiche 1 + 2 * 3;`
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

        LisaCompiler compiler = new LisaCompiler();

        // when :
        Chunk chunk = compiler.compile(ast);

        // then :
        List<Object> constants = chunk.getConstants();
        assertEquals(3, constants.size(), "On doit avoir 3 constantes (1, 2, 3)");
        assertEquals(1f, (Float) constants.get(0));
        assertEquals(2f, (Float) constants.get(1));
        assertEquals(3f, (Float) constants.get(2));

        // then :
        List<Integer> code = chunk.getCode();

        // CONSTANT 0   (push 1)
        // CONSTANT 1   (push 2)
        // CONSTANT 2   (push 3)
        // MULTIPLY     (2 * 3)
        // ADD          (1 + (2*3))
        // PRINT
        // RETURN
        List<Integer> expectedCode = List.of(
                OpCode.CONSTANT.ordinal(), 0,
                OpCode.CONSTANT.ordinal(), 1,
                OpCode.CONSTANT.ordinal(), 2,
                OpCode.MULTIPLY.ordinal(),
                OpCode.ADD.ordinal(),
                OpCode.PRINT.ordinal(),
                OpCode.RETURN.ordinal()
        );

        assertEquals(expectedCode, code, "Le bytecode pour `affiche 1 + 2 * 3;` n'est pas correct");
    }
}