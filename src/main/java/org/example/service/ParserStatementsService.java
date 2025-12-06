package org.example.service;

import org.example.Parser;
import org.example.exceptions.SyntaxError;
import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.example.utils.TokenReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParserStatementsService {

    private Parser parser;
    private TokenReader tokenReader;

    public ParserStatementsService(Parser parser, TokenReader tokenReader) {
        this.parser= parser;
        this.tokenReader = tokenReader;
    }

    public List<Statement> parse(){
        List<Statement> statements = new ArrayList<>();
        while(!tokenReader.is_at_end()){
            try {
                statements.add(parse_declaration());
            } catch (SyntaxError e) {
                System.out.println(e.getMessage());
                synchronize();
            }
        }
        return statements;
    }

    public Statement parse_declaration(){
        if(tokenReader.match(TokenType.FN))
            return parse_function_declaration();
        if(tokenReader.match(TokenType.VAR))
            return parse_var_declaration();
        return parse_statement();
    }

    private Statement parse_var_declaration() {
        Token tokenName = tokenReader.consume(TokenType.IDENTIFIER, "Expect variable name.");
        Expression initializer = null;
        if(tokenReader.match(TokenType.EQUAL))
            initializer = parser.parseExpression();
        tokenReader.consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new VarStatement(tokenName, initializer);
    }

    public Statement parse_statement() {
        if(tokenReader.match(TokenType.PRINT))
            return parse_print_statementt();
        else if(tokenReader.match(TokenType.IF))
            return parse_if_statementt();
        else if(tokenReader.match(TokenType.LBRACE))
            return parse_block_statement();
        else if(tokenReader.match(TokenType.WHILE))
            return parse_while_statementt();
        else if(tokenReader.match(TokenType.RETURN))
            return parse_return_statement();
        else if(tokenReader.match(TokenType.IMPORT) )
            return parseImportStatement();
        return parse_expression_statements();
    }

    private Statement parse_print_statementt() {
        Expression expr = parser.parseExpression();
        tokenReader.consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new PrintStatement(expr);
    }

    private Statement parse_if_statementt() {
        tokenReader.consume(TokenType.LPAREN, "Expect '(' after 'if'.");
        Expression condition = parser.parseExpression();
        tokenReader.consume(TokenType.RPAREN, "Expect ')' after 'if'.");
        Statement then_statement = parse_statement();
        Statement else_staement = null;
        if(tokenReader.match(TokenType.ELSE))
            else_staement = parse_statement();
        return new IfStatement(condition, then_statement, Optional.ofNullable(else_staement));
    }

    private Statement parse_while_statementt() {
        tokenReader.consume(TokenType.LPAREN, "Expect '(' after 'while'.");
        Expression condition = parser.parseExpression();
        tokenReader.consume(TokenType.RPAREN, "Expect ')' after 'while'.");
        Statement body = parse_statement();
        return new WhileStatement(condition, body);
    }

    private Statement parse_return_statement() {
        Token keyword = tokenReader.previous();
        Expression value = null;
        if(!tokenReader.check(TokenType.SEMICOLON))
            value = parser.parseExpression();
        tokenReader.consume(TokenType.SEMICOLON, "Expect ';' after 'return' value.");
        return new ReturnStatement(keyword, Optional.ofNullable(value));
    }

    private Statement parse_block_statement() {
        List<Statement> statements = new ArrayList<>();
        while(!tokenReader.check(TokenType.RBRACE)){
            statements.add(parse_declaration());
        }
        tokenReader.consume(TokenType.RBRACE, "Expect '}' after block.");
        return new BlockStatement(statements);
    }

    private Statement parse_expression_statements() {
        Expression expr = parser.parseExpression();
        tokenReader.consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStatement(expr);
    }

    private void synchronize() {
        while (!tokenReader.is_at_end()) {
            Token previous = tokenReader.previous();
            if (previous.getType() == TokenType.SEMICOLON ||
                    previous.getType() == TokenType.RBRACE) {
                return;
            }
            TokenType currentType = tokenReader.peek().getType();
            switch (currentType) {
                case IF, WHILE, PRINT, VAR, FN, RETURN -> {
                    return;
                }
            }
            tokenReader.advance();
        }
    }

    private Statement parse_function_declaration() {
        Token name = tokenReader.consume(TokenType.IDENTIFIER, "Expect function name.");
        tokenReader.consume(TokenType.LPAREN, "Expect '(' after function name.");

        List<Token> parameters = new ArrayList<>();
        if (!tokenReader.check(TokenType.RPAREN)) {
            do {
                Token param = tokenReader.consume(TokenType.IDENTIFIER, "Expect parameter name.");
                parameters.add(param);
            } while (tokenReader.match(TokenType.AND));
        }

        tokenReader.consume(TokenType.RPAREN, "Expect ')' after parameters.");
        tokenReader.consume(TokenType.LBRACE, "Expect 'debut' before function body.");

        List<Statement> body = new ArrayList<>();
        while (!tokenReader.check(TokenType.RBRACE) && !tokenReader.is_at_end()) {
            body.add(parse_declaration());
        }

        tokenReader.consume(TokenType.RBRACE, "Expect 'fin' after function body.");

        return new FunctionDeclarationStatement(name, parameters, body);
    }
    private Statement parseImportStatement() {
        Token pathTok = tokenReader.advance();
        if (pathTok.getType() != TokenType.STRING) {
            throw new SyntaxError("Chemin d'import attendu", pathTok.getLine(), pathTok.getLexeme());
        }
        if (!tokenReader.match(TokenType.SEMICOLON)) {
            throw new SyntaxError("Point-virgule attendu après import", pathTok.getLine(), pathTok.getLexeme());
        }
        return new ImportStatement(pathTok.getLexeme());
    }
}