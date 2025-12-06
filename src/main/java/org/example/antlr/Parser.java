package org.example.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.exceptions.SyntaxError;
import org.example.generatedParser.LisaLexer;
import org.example.generatedParser.LisaParser;
import org.example.model.ast.Statement;

import java.util.List;

public class Parser {
    private final String source;

    public Parser(String source) {
        this.source = source;
    }

    public List<Statement> parse() {
        CharStream input = CharStreams.fromString(source);
        LisaLexer lexer = new LisaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LisaParser parser = new LisaParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                String literal = offendingSymbol instanceof Token ? ((Token) offendingSymbol).getText() : "";
                throw new SyntaxError(msg, line, literal);
            }
        });

        ParseTree tree = parser.programme();
        LISAASTBuilder builder = new LISAASTBuilder();
        @SuppressWarnings("unchecked")
        List<Statement> result = (List<Statement>) builder.visit(tree);
        return result;
    }
}
