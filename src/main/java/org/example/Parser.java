package org.example;

import org.example.exceptions.RuntimeError;
import org.example.exceptions.SyntaxError;
import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public List<Token> getTokens() {
        return tokens;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    private final Map<TokenType, PrefixParselet> prefixParselets = new HashMap<>();
    private final Map<TokenType, InfixParselet> infixParselets = new HashMap<>();

    private static final int PREC_LOWEST     = 0;
    private static final int PREC_EQUALITY   = 1;
    private static final int PREC_COMPARISON   = 2;
    private static final int PREC_TERM       = 3;
    private static final int PREC_FACTOR     = 4;
    private static final int PREC_PREFIX     = 5;
    private static final int PREC_CALL       = 6;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        registerParselets();
    }

    private void registerParselets() {
        prefixParselets.put(TokenType.NUMBER, new NumberParselet());
        prefixParselets.put(TokenType.STRING, new StringParselet());
        prefixParselets.put(TokenType.IDENTIFIER, new IdentifierParselet());
        prefixParselets.put(TokenType.MINUS, new PrefixUnaryParselet());
        prefixParselets.put(TokenType.BANG, new PrefixUnaryParselet());
        prefixParselets.put(TokenType.LPAREN, new GroupingParselet());
        prefixParselets.put(TokenType.MATCH, new MatchParselet());

        infixParselets.put(TokenType.PLUS, new BinaryOperatorParselet(PREC_TERM, false));
        infixParselets.put(TokenType.MINUS, new BinaryOperatorParselet(PREC_TERM, false));
        infixParselets.put(TokenType.STAR, new BinaryOperatorParselet(PREC_FACTOR, false));
        infixParselets.put(TokenType.SLASH, new BinaryOperatorParselet(PREC_FACTOR, false));
        infixParselets.put(TokenType.EQUAL_EQUAL, new BinaryOperatorParselet(PREC_EQUALITY, false));
        infixParselets.put(TokenType.LESS, new BinaryOperatorParselet(PREC_COMPARISON, false));
        infixParselets.put(TokenType.LESS_EQUAL, new BinaryOperatorParselet(PREC_COMPARISON, false));
        infixParselets.put(TokenType.GREATER, new BinaryOperatorParselet(PREC_COMPARISON, false));
        infixParselets.put(TokenType.GREATER_EQUAL, new BinaryOperatorParselet(PREC_COMPARISON, false));

        infixParselets.put(TokenType.LPAREN, new CallParselet(PREC_CALL));

        if (hasTokenType(TokenType.BANG_EQUAL)) {
            infixParselets.put(TokenType.BANG_EQUAL, new BinaryOperatorParselet(PREC_EQUALITY, false));
        }
    }

    private boolean hasTokenType(TokenType t) {
        try {
            TokenType.valueOf(t.name());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public Expression parseExpression() {
        return parseAssignment();
    }

    public Expression parseAssignment(){
        Expression expression = parsePrecedence(PREC_LOWEST);
        if(match(TokenType.EQUAL) || match(TokenType.ASSIGN)){
            Token equals = previous();
            Expression value = parseAssignment();
            if(expression instanceof Variable){
                Token name = ((Variable) expression).getName();
                return new VariableAssignment(name, value);
            }
            throw new SyntaxError(
                    "Impossible d'assigner une valeur ici",
                    equals.getLine(),
                    equals.getLexeme()
            );
        }
        return expression;
    }
    private Expression parsePrecedence(int precedenceMin) {
        Token token = advance();

        PrefixParselet prefix = prefixParselets.get(token.getType());
        if (prefix == null) {
            throw new SyntaxError(
                    "L'expression est invalide: le prefixe est inconnu",
                    token.getLine(),
                    token.getLexeme()
            );
        }

        Expression left = prefix.parse(this, token);

        while (!isAtEnd()) {
            TokenType nextType = peek().getType();
            InfixParselet infix = infixParselets.get(nextType);
            if (infix == null) break;

            int prec = infix.getPrecedence();
            if (prec <= precedenceMin) break;

            Token operator = advance();
            left = infix.parse(this, left, operator);
        }

        return left;
    }

    private interface PrefixParselet {
        Expression parse(Parser parser, Token token);
    }

    private interface InfixParselet {
        Expression parse(Parser parser, Expression left, Token operator);
        int getPrecedence();
    }

    private static class NumberParselet implements PrefixParselet {
        @Override
        public Expression parse(Parser parser, Token token) {
            String lex = token.getLexeme();
            try {
                float value = Float.parseFloat(lex);
                return new Literal(value);
            } catch (NumberFormatException ex) {
                throw new SyntaxError(
                        "Impossible de parser le nombre ",
                        token.getLine(),
                        lex
                );
            }
        }
    }

    private static class StringParselet implements PrefixParselet {
        @Override
        public Expression parse(Parser parser, Token token) {
            String lex = token.getLexeme();
            try {
                return new Literal(lex);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("StringParselet non implémenté avec les nouvelles classes AST" + lex);
            }
        }
    }

    private static class IdentifierParselet implements PrefixParselet {
        @Override
        public Expression parse(Parser parser, Token token) {
            return new Variable(token);
        }
    }

    private static class PrefixUnaryParselet implements PrefixParselet {
        @Override
        public Expression parse(Parser parser, Token token) {
            Expression right = parser.parsePrecedence(PREC_PREFIX);
            return new Unary(token, right);
        }
    }

    private static class GroupingParselet implements PrefixParselet {
        @Override
        public Expression parse(Parser parser, Token token) {
            Expression expr = parser.parseExpression();
            parser.consume(TokenType.RPAREN, "')' attendu après l'expression entre parenthèses");
            return expr;
        }
    }

    private static class BinaryOperatorParselet implements InfixParselet {
        private final int precedence;
        private final boolean rightAssociative;

        public BinaryOperatorParselet(int precedence, boolean rightAssociative) {
            this.precedence = precedence;
            this.rightAssociative = rightAssociative;
        }

        @Override
        public int getPrecedence() {
            return precedence;
        }

        @Override
        public Expression parse(Parser parser, Expression left, Token operator) {
            int nextPrecedence = rightAssociative ? (precedence - 1) : precedence;
            Expression right = parser.parsePrecedence(nextPrecedence);
            return new Binary(left, operator, right);
        }
    }

    private static class CallParselet implements InfixParselet {
        private final int precedence;

        public CallParselet(int precedence) {
            this.precedence = precedence;
        }

        @Override
        public int getPrecedence() {
            return precedence;
        }

        @Override
        public Expression parse(Parser parser, Expression left, Token operator) {
            List<Expression> arguments = new ArrayList<>();

            if (!parser.check(TokenType.RPAREN)) {
                do {
                    arguments.add(parser.parseExpression());
                } while (parser.match(TokenType.AND));
            }

            parser.consume(TokenType.RPAREN, "')' attendu après les arguments");
            return new FunctionCall(left, arguments);
        }
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    public boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new SyntaxError(
                message,
                null,
                String.valueOf(peek())
        );
    }

    private RuntimeException error(Token token, String message) {
        throw new RuntimeError(
                "Erreur de syntaxe",
                token.getLine(),
                message
        );
    }

    public static String parseToString(List<Token> tokens) {
        Parser p = new Parser(tokens);
        Expression e = p.parseExpression();
        return e.toString();
    }

    public class MatchParselet implements Parser.PrefixParselet {

        @Override
        public Expression parse(Parser parser, Token matchToken) {

            Expression value = parser.parseExpression();

            parser.consume(TokenType.LBRACE, "Expect '{' after match expression.");

            List<CaseBranch> cases = new ArrayList<>();
            Expression defaultCase = null;

            while (!parser.check(TokenType.RBRACE)) {

                if (parser.match(TokenType.DEFAULT)) {
                    parser.consume(TokenType.ARROW, "Expect '->' after default.");
                    defaultCase = parser.parseExpression();
                } else {
                    Expression pattern = parser.parseExpression();
                    parser.consume(TokenType.ARROW, "Expect '->' after pattern.");
                    Expression body = parser.parseExpression();
                    cases.add(new CaseBranch(pattern, body));
                }

                parser.match(TokenType.COMMA);
            }

            parser.consume(TokenType.RBRACE, "Expect '}' after match block.");

            return new MatchExpression(value, cases, defaultCase);
        }
    }



}