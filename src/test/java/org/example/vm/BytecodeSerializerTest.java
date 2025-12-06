package org.example.vm;

import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BytecodeSerializerTest {

    @Test
    void itShouldSaveAndLoadBytecodeAndRunInVM() throws Exception {
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

        // Compilation : AST -> Chunk
        LisaCompiler compiler = new LisaCompiler();
        Chunk originalChunk = compiler.compile(ast);

        // Temp file to store bytecode
        Path tempFile = Files.createTempFile("lisa_test_", ".lbc");
        String path = tempFile.toFile().getAbsolutePath();

        // when : serialize and deserialize
        BytecodeSerializer.saveBytecode(originalChunk, path);
        Chunk loadedChunk = BytecodeSerializer.loadBytecode(path);

        // then 1 : check it's the same size
        assertEquals(
                originalChunk.getCodeSize(),
                loadedChunk.getCodeSize(),
                "La taille du code doit être la même après sérialisation"
        );

        // then 2 : check we have the same instructions
        List<Integer> originalCode = originalChunk.getCode();
        List<Integer> loadedCode = loadedChunk.getCode();
        assertEquals(originalCode, loadedCode, "Le bytecode doit être identique après rechargement");

        // then 3 : check we have the same values in constants
        // ( types can be Float -> Double)
        List<Object> originalConstants = originalChunk.getConstants();
        List<Object> loadedConstants = loadedChunk.getConstants();
        assertEquals(
                originalConstants.size(),
                loadedConstants.size(),
                "Le nombre de constantes doit être identique"
        );

        for (int i = 0; i < originalConstants.size(); i++) {
            Object orig = originalConstants.get(i);
            Object reloaded = loadedConstants.get(i);

            assertTrue(orig instanceof Number, "La constante originale doit être un nombre");
            assertTrue(reloaded instanceof Number, "La constante rechargée doit être un nombre");

            double origVal = ((Number) orig).doubleValue();
            double reloadedVal = ((Number) reloaded).doubleValue();

            assertEquals(origVal, reloadedVal, 1e-9, "La valeur de la constante " + i + " doit être identique");
        }

        // then 4 : running in the VM to check it works
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            VM vm = new VM();
            Object result = vm.run(loadedChunk);
            assertNull(result, "La VM ne doit pas retourner de valeur dans ce cas (PRINT vide la stack)");
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString().trim();
        assertEquals("7.0", output, "La VM doit afficher '7.0' après rechargement du bytecode");

        // Optionnel : delete the temp file
        Files.deleteIfExists(tempFile);
    }
}
