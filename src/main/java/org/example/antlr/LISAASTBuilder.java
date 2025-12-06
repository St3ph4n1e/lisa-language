package org.example.antlr;

import org.antlr.v4.runtime.Token;
import org.example.generatedParser.LisaBaseVisitor;
import org.example.generatedParser.LisaParser;
import org.example.model.TokenType;
import org.example.model.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LISAASTBuilder extends LisaBaseVisitor<Object> {
    @Override
    public Object visitProgramme(LisaParser.ProgrammeContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (LisaParser.InstructionContext ic : ctx.instruction()) {
            Statement s = (Statement) visit(ic);
            if (s != null) statements.add(s);
        }
        return statements;
    }

    @Override
    public Object visitInstruction(LisaParser.InstructionContext ctx) {
        if (ctx.declaration() != null) return visit(ctx.declaration());
        if (ctx.affectation() != null) return visit(ctx.affectation());
        if (ctx.affichage() != null) return visit(ctx.affichage());
        if (ctx.conditionnel() != null) return visit(ctx.conditionnel());
        if (ctx.boucle() != null) return visit(ctx.boucle());
        if (ctx.fonction_declaration() != null) return visit(ctx.fonction_declaration());
        if (ctx.retour() != null) return visit(ctx.retour());
        if (ctx.appel_fonction() != null) return visit(ctx.appel_fonction());
        if (ctx.bloc() != null) return visit(ctx.bloc());
        return null;
    }

    @Override
    public Object visitBloc(LisaParser.BlocContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (LisaParser.InstructionContext ic : ctx.instruction()) {
            statements.add((Statement) visit(ic));
        }
        return new BlockStatement(statements);
    }

    @Override
    public Object visitDeclaration(LisaParser.DeclarationContext ctx) {
        Token idTok = ctx.IDENTIFIANT().getSymbol();
        org.example.model.Token name = new org.example.model.Token(TokenType.IDENTIFIER, idTok.getText(), idTok.getLine());
        Expression init = (Expression) visit(ctx.expression());
        return new VarStatement(name, init);
    }

    @Override
    public Object visitAffectation(LisaParser.AffectationContext ctx) {
        Token idTok = ctx.IDENTIFIANT().getSymbol();
        org.example.model.Token name = new org.example.model.Token(TokenType.IDENTIFIER, idTok.getText(), idTok.getLine());
        Expression value = (Expression) visit(ctx.expression());
        return new ExpressionStatement(new VariableAssignment(name, value));
    }

    @Override
    public Object visitAffichage(LisaParser.AffichageContext ctx) {
        Expression e = (Expression) visit(ctx.expression());
        return new PrintStatement(e);
    }

    @Override
    public Object visitConditionnel(LisaParser.ConditionnelContext ctx) {
        Expression cond = (Expression) visit(ctx.condition());
        Statement thenBranch = (Statement) visit(ctx.bloc(0));
        Statement elseBranch = null;
        if (ctx.SINON() != null && ctx.bloc().size() > 1) {
            elseBranch = (Statement) visit(ctx.bloc(1));
        }
        return new IfStatement(cond, thenBranch, Optional.ofNullable(elseBranch));
    }

    @Override
    public Object visitBoucle(LisaParser.BoucleContext ctx) {
        Expression cond = (Expression) visit(ctx.condition());
        Statement body = (Statement) visit(ctx.bloc());
        return new WhileStatement(cond, body);
    }

    @Override
    public Object visitFonction_declaration(LisaParser.Fonction_declarationContext ctx) {
        Token nameTok = ctx.IDENTIFIANT().getSymbol();
        org.example.model.Token name = new org.example.model.Token(TokenType.IDENTIFIER, nameTok.getText(), nameTok.getLine());
        List<org.example.model.Token> params = new ArrayList<>();
        if (ctx.parametres() != null) {
            for (var t : ctx.parametres().IDENTIFIANT()) {
                Token p = t.getSymbol();
                params.add(new org.example.model.Token(TokenType.IDENTIFIER, p.getText(), p.getLine()));
            }
        }
        BlockStatement body = (BlockStatement) visit(ctx.bloc());
        return new FunctionDeclarationStatement(name, params, body.getStatements());
    }

    @Override
    public Object visitRetour(LisaParser.RetourContext ctx) {
        int line = ctx.getStart().getLine();
        org.example.model.Token keyword = new org.example.model.Token(TokenType.RETURN, ctx.RETOURNE().getText(), line);
        Expression value = (Expression) visit(ctx.expression());
        return new ReturnStatement(keyword, Optional.ofNullable(value));
    }

    @Override
    public Object visitCondition(LisaParser.ConditionContext ctx) {
        Expression left = (Expression) visit(ctx.expression(0));
        org.example.model.Token op = mapComparator(ctx.comparateur());
        Expression right = (Expression) visit(ctx.expression(1));
        return new Binary(left, op, right);
    }

    private org.example.model.Token mapComparator(LisaParser.ComparateurContext ctx) {
        int line = ctx.getStart().getLine();
        if (ctx.EGALE() != null) return new org.example.model.Token(TokenType.EQUAL_EQUAL, ctx.EGALE().getText(), line);
        if (ctx.PLUS_PETIT_QUE() != null) return new org.example.model.Token(TokenType.LESS, ctx.PLUS_PETIT_QUE().getText(), line);
        if (ctx.PLUS_GRAND_QUE() != null) return new org.example.model.Token(TokenType.GREATER, ctx.PLUS_GRAND_QUE().getText(), line);
        return new org.example.model.Token(TokenType.EQUAL_EQUAL, "egale", line);
    }

    @Override
    public Object visitAppel_fonction(LisaParser.Appel_fonctionContext ctx) {
        // IDENTIFIANT EST IDENTIFIANT '(' arguments? ')' ';'?
        Token targetTok = ctx.IDENTIFIANT(0).getSymbol();
        org.example.model.Token target = new org.example.model.Token(TokenType.IDENTIFIER, targetTok.getText(), targetTok.getLine());
        Expression callee = new Variable(new org.example.model.Token(TokenType.IDENTIFIER, ctx.IDENTIFIANT(1).getText(), ctx.IDENTIFIANT(1).getSymbol().getLine()));
        List<Expression> args = new ArrayList<>();
        if (ctx.arguments() != null) {
            args = (List<Expression>) visit(ctx.arguments());
        }
        Expression call = new FunctionCall(callee, args);
        return new ExpressionStatement(new VariableAssignment(target, call));
    }

    @Override
    public Object visitArguments(LisaParser.ArgumentsContext ctx) {
        List<Expression> args = new ArrayList<>();
        for (var ectx : ctx.expression()) {
            args.add((Expression) visit(ectx));
        }
        return args;
    }

    @Override
    public Object visitExpression(LisaParser.ExpressionContext ctx) {
        // Parenthesized
        if (ctx.PAREN_OUVRANTE() != null && ctx.PAREN_FERMANTE() != null && ctx.expression().size() == 1) {
            return visit(ctx.expression(0));
        }
        // Function call in expression: IDENTIFIANT '(' arguments? ')'
        if (ctx.IDENTIFIANT() != null && ctx.PAREN_OUVRANTE() != null && ctx.PAREN_FERMANTE() != null && ctx.operateur() == null) {
            org.example.model.Token id = new org.example.model.Token(TokenType.IDENTIFIER, ctx.IDENTIFIANT().getText(), ctx.IDENTIFIANT().getSymbol().getLine());
            Expression callee = new Variable(id);
            List<Expression> args = new ArrayList<>();
            if (ctx.arguments() != null) args = (List<Expression>) visit(ctx.arguments());
            return new FunctionCall(callee, args);
        }
        // IDENTIFIANT
        if (ctx.IDENTIFIANT() != null && ctx.operateur() == null && ctx.expression().isEmpty()) {
            Token t = ctx.IDENTIFIANT().getSymbol();
            return new Variable(new org.example.model.Token(TokenType.IDENTIFIER, t.getText(), t.getLine()));
        }
        // NOMBRE
        if (ctx.NOMBRE() != null) {
            String text = ctx.NOMBRE().getText();
            float num = Float.parseFloat(text);
            return new Literal(num);
        }
        // Binary: expression operateur expression (left recursion will nest)
        if (ctx.expression().size() == 2 && ctx.operateur() != null) {
            Expression left = (Expression) visit(ctx.expression(0));
            org.example.model.Token op = (org.example.model.Token) visit(ctx.operateur());
            Expression right = (Expression) visit(ctx.expression(1));
            return new Binary(left, op, right);
        }
        return super.visitExpression(ctx);
    }

    @Override
    public Object visitOperateur(LisaParser.OperateurContext ctx) {
        int line = ctx.getStart().getLine();
        if (ctx.PLUS() != null) return new org.example.model.Token(TokenType.PLUS, "+", line);
        if (ctx.MOINS() != null) return new org.example.model.Token(TokenType.MINUS, "-", line);
        if (ctx.FOIS() != null) return new org.example.model.Token(TokenType.STAR, "*", line);
        if (ctx.DIVISE() != null) return new org.example.model.Token(TokenType.SLASH, "/", line);
        return new org.example.model.Token(TokenType.PLUS, "+", line);
    }
}
