package org.example;

import org.example.service.ParserStatementsService;
import org.example.utils.TokenReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.example.model.Token;
import org.example.model.ast.ImportStatement;
import org.example.model.ast.Statement;
import java.util.List;

class ImportSystemTest {
    @Test
    void testParseImportStatement() {
        String source = "import \"utils\";";
        ParserStatementsService parserStatementsService = initializerTest(source);
        Statement stmt = parserStatementsService.parse_statement();
        assertInstanceOf(ImportStatement.class, stmt, "Le statement doit être un ImportStatement");
        ImportStatement importStmt = (ImportStatement) stmt;

        assertEquals("utils", importStmt.getPath());
    }


    @Test
    void testImportModuleDefinesVariable() throws IOException {
        Path tempFile = Files.createTempFile("module", ".lisa");
        Files.writeString(tempFile, "variable x est 42;");
        String source = "import \"" + tempFile + "\"; affiche x;";
        ParserStatementsService parserStatementsService = initializerTest(source);
        List<Statement> ast = parserStatementsService.parse();
        Interpreter interpreter = new Interpreter();
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        interpreter.interpret(ast, 0);

        assertTrue(out.toString().contains("42"));
    }

    @Test
    void testImportCycleCausesStackOverflow() throws IOException {
        Path fileA = Files.createTempFile("A", ".lisa");
        Path fileB = Files.createTempFile("B", ".lisa");

        Files.writeString(fileA, "import \"" + fileB.toString() + "\";");
        Files.writeString(fileB, "import \"" + fileA + "\";");

        String source = "import \"" + fileA + "\";";
        Lexer lexer = new Lexer(source);
        ParserStatementsService parserStatementsService = initializerTest(source);

        List<Statement> ast = parserStatementsService.parse();

        Interpreter interpreter = new Interpreter();

        assertThrows(StackOverflowError.class, () -> interpreter.interpret(ast, 0));
    }

    // Test ModuleLoader
    @Test
    void testLoadModuleWithAbsolutePath() throws IOException {
        Path tempFile = Files.createTempFile("module", ".txt");
        Files.writeString(tempFile, "contenu absolu");

        String result = ModuleLoader.loadModule(tempFile.toString(), tempFile.toString());

        assertEquals("contenu absolu", result);
    }

    @Test
    void testLoadModuleWithRelativePath() throws IOException {
        Path currentFile = Files.createTempFile("current", ".txt");
        Path dir = currentFile.getParent();
        Path relativeFile = dir.resolve("relativeModule.txt");
        Files.writeString(relativeFile, "contenu relatif");

        String result = ModuleLoader.loadModule(currentFile.toString(), "relativeModule.txt");

        assertEquals("contenu relatif", result);
    }

    @Test
    void testLoadModuleFileNotFoundThrowsIOException() {
        Path currentFile = Path.of("fakeFile.txt");

        assertThrows(IOException.class, () ->
                ModuleLoader.loadModule(currentFile.toString(), "doesNotExist.txt")
        );
    }

    private ParserStatementsService initializerTest(String source){
        Lexer lexer = new Lexer(source);
        List<Token> tokensResult = lexer.tokenize();
        Parser parser = new Parser(tokensResult);
        TokenReader tokenReader = new TokenReader(parser);
        return new ParserStatementsService(parser, tokenReader);
    }
}
