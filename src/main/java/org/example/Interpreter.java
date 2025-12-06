package org.example;

import org.example.exceptions.RuntimeError;
import org.example.exceptions.ReturnException;
import org.example.model.Token;
import org.example.model.TokenType;
import org.example.model.ast.*;
import org.example.service.ParserStatementsService;
import org.example.utils.TokenReader;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {
    private Environment environment;

    public Interpreter() {
        this.environment = new Environment(null);
    }

    public void interpret(List<Statement> statements, int startIndex) {
        for (int i = startIndex; i < statements.size(); i++) {
            Statement statement = statements.get(i);
            this.execute(statement);
        }
    }

    private void execute(Statement stmt) {
        switch (stmt) {
            case ExpressionStatement exprStmt -> this.evaluate(exprStmt.getExpression());
            case VarStatement varStmt -> {
                Object value = null;
                if (varStmt.getInitializer() != null) {
                    value = this.evaluate(varStmt.getInitializer());
                }
                this.environment.define(varStmt.getName().getLexeme(), value);
            }
            case FunctionDeclarationStatement funcDeclarationStmt -> {
                LisaFunction function = new LisaFunction(this, funcDeclarationStmt, new Environment(this.environment));
                this.environment.define(funcDeclarationStmt.getName().getLexeme(), function);
            }
            case PrintStatement printStmt -> {
                Object value = this.evaluate(printStmt.getExpression());
                System.out.println(value);
            }
            case IfStatement is -> {
                Object conditionValue = evaluate(is.getCondition());
                if (conditionValue instanceof Boolean && (Boolean) conditionValue) {
                    execute(is.getThenBranch());
                } else if (is.getElseBranch().isPresent()) {
                    execute(is.getElseBranch().get());
                }
            }
            case WhileStatement ws -> {
                while (this.evaluate(ws.getCondition()).equals(true)) {
                    this.execute(ws.getBody());
                }
            }
            case ReturnStatement rs -> {
                Object value = null;
                if (rs.getValue().isPresent()) {
                    value = evaluate(rs.getValue().get());
                }
                throw new ReturnException(value);
            }
            case BlockStatement bs -> executeBlock(bs.getStatements(), new Environment(environment));
            case ImportStatement importStmt -> {
                importModule(importStmt.getPath());
            }
            default -> throw new RuntimeError(
                    "Instruction inconnue",
                    null,
                    stmt.getClass().getSimpleName()
            );
        }
    }

    private boolean isNumber(Object value) {
        return value instanceof Number;
    }

    private double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new RuntimeError(
                "La valeur n'est pas de type nombre",
                null,
                String.valueOf(value)
        );
    }

    private Object evaluate(Expression expr) {
        switch (expr) {
            case Literal literal -> {
                return literal.getValue();
            }
            case Binary binary -> {
                Object left = evaluate(binary.getLeft());
                Object right = evaluate(binary.getRight());

                TokenType operator = binary.getOperator().getType();

                switch (operator) {
                    case TokenType.PLUS -> {
                        if (isNumber(left) && isNumber(right)) {
                            return toDouble(left) + toDouble(right);
                        } else if (left instanceof String && right instanceof String) {
                            return String.valueOf(left) + String.valueOf(right);
                        } else {
                            throw new RuntimeError(
                                    "Impossible d'additionner ces valeurs",
                                    null,
                                    left + " + " + right
                            );
                        }
                    }
                    case TokenType.MINUS -> {
                        checkNumbers(left, right);
                        return toDouble(left) - toDouble(right);
                    }
                    case TokenType.STAR -> {
                        checkNumbers(left, right);
                        return toDouble(left) * toDouble(right);
                    }
                    case TokenType.SLASH -> {
                        checkNumbers(left, right);
                        if (toDouble(right) == 0) {
                            throw new RuntimeError(
                                    "Division par zéro impossible",
                                    null,
                                    left + " / 0"
                            );
                        }
                        return toDouble(left) / toDouble(right);
                    }
                    case TokenType.EQUAL_EQUAL -> {
                        checkNumbers(left, right);
                        return toDouble(left) == toDouble(right);
                    }
                    case TokenType.BANG_EQUAL -> {
                        checkNumbers(left, right);
                        return toDouble(left) != toDouble(right);
                    }
                    case TokenType.GREATER -> {
                        checkNumbers(left, right);
                        return toDouble(left) > toDouble(right);
                    }
                    case TokenType.GREATER_EQUAL -> {
                        checkNumbers(left, right);
                        return toDouble(left) >= toDouble(right);
                    }
                    case TokenType.LESS -> {
                        checkNumbers(left, right);
                        return toDouble(left) < toDouble(right);
                    }
                    case TokenType.LESS_EQUAL -> {
                        checkNumbers(left, right);
                        return toDouble(left) <= toDouble(right);
                    }
                    default -> throw new RuntimeError(
                            "Opérateur inconnu",
                            null,
                            operator.toString()
                    );
                }
            }
            case Unary unary -> {
                Object right = evaluate(unary.getRight());
                TokenType operator = unary.getOperator().getType();
                switch (operator) {
                    case TokenType.MINUS -> {
                        if (right instanceof Number) {
                            return toDouble(right) * -1;
                        } else {
                            throw new RuntimeError(
                                    "L'opérateur de droite n'est pas de type nombre",
                                    null,
                                    String.valueOf(right)
                            );
                        }
                    }
                    case TokenType.BANG -> {
                        if (right instanceof Boolean) {
                            return !((Boolean) right);
                        } else {
                            throw new RuntimeError(
                                    "L'opérateur de droite n'est pas de type nombre",
                                    null,
                                    String.valueOf(right)
                            );
                        }
                    }
                    default -> throw new RuntimeError(
                            "Opérateur inconnu",
                            null,
                            operator.toString()
                    );
                }
            }
            case VariableAssignment varAssignment -> {
                Object value = this.evaluate(varAssignment.getValue());
                this.environment.assign(varAssignment.getName().getLexeme(), value);
                return value;
            }
            case FunctionCall functionCall -> {
                Object callee = this.evaluate(functionCall.getCallee());
                if (!(callee instanceof LisaFunction)) {
                    throw new RuntimeError(
                            "La fonction appelée n'existe pas",
                            null,
                            functionCall.getCallee().toString()
                    );
                }

                List<Object> arguments = new ArrayList<>();
                for (Expression arg : functionCall.getArguments()) {
                    arguments.add(evaluate(arg));
                }

                LisaFunction function = (LisaFunction) callee;
                return function.call(arguments);
            }
            case Variable var -> {
                return this.environment.get(var.getName().getLexeme());
            }
            case MatchExpression matchExpr -> {
                Object value = evaluate(matchExpr.getValue());

                for (CaseBranch caseBranch : matchExpr.getCases()) {
                    Object patternValue = evaluate(caseBranch.getPattern());

                    if (equalsValue(value, patternValue)) {
                        return evaluate(caseBranch.getBody());
                    }
                }

                if (matchExpr.getDefaultCase() != null) {
                    return evaluate(matchExpr.getDefaultCase());
                }

                throw new RuntimeError(
                        "Aucun case ne correspond.",
                        null,
                        value.toString()
                );
            }
            default -> throw new RuntimeError("Expression inconnue",
                    null,
                    expr.toString()
            );
        }
    }
    private boolean equalsValue(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a instanceof Number && b instanceof Number) {
            return toDouble(a) == toDouble(b);
        }
        return a.equals(b);
    }

    public void executeBlock(List<Statement> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Statement statement : statements) {
                this.execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    private void importModule(String path) {
        Path resolved = resolvePath(path);
        try {
            String source = Files.readString(resolved);

            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
            TokenReader tokenReader = new TokenReader(parser);
            ParserStatementsService parserStatementsService = new ParserStatementsService(parser, tokenReader);
            List<Statement> statements = parserStatementsService.parse();
            while (!parser.isAtEnd()) {
                statements.add(parserStatementsService.parse_statement());
            }

            this.interpret(statements, 0);

        } catch (IOException e) {
            throw new RuntimeError("Impossible de lire le module : " + path, null, path);
        }
    }

    private Path resolvePath(String path) {
        Path p = Paths.get(path);
        if (!p.isAbsolute()) {
            p = Paths.get(System.getProperty("user.dir")).resolve(p);
        }
        return p.normalize();
    }


    private void checkNumbers(Object left, Object right) {
        if (!isNumber(left)) {
            throw new RuntimeError(
                    "La valeur de gauche doit être de type nombre",
                    null,
                    String.valueOf(left)
            );
        }
        if (!isNumber(right)) {
            throw new RuntimeError(
                    "La valeur de droite doit être de type nombre",
                    null,
                    String.valueOf(right)
            );
        }
    }
}