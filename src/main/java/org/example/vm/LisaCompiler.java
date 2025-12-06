package org.example.vm;

import org.example.exceptions.RuntimeError;
import org.example.model.TokenType;
import org.example.model.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LisaCompiler {

    private final Chunk chunk;
    private final Map<String, Integer> globalNameConstants = new HashMap<>();

    public LisaCompiler() {
        this.chunk = new Chunk();
    }

    // Compile a list of instructions from Lisa AST in bytecode --> Chunk
    public Chunk compile(List<Statement> statements) {
        for (Statement statement : statements) {
            compileStatement(statement);
        }
        // End of program --> RETURN
        chunk.writeOp(OpCode.RETURN);
        return chunk;
    }

    //Compile one instruction ( Statement) in Bytecode
    private void compileStatement(Statement statement) {
        switch (statement) {
            case PrintStatement ps -> {
                // we print the expression
                compileExpression(ps.getExpression());
                chunk.writeOp(OpCode.PRINT);
            }
            case ExpressionStatement es -> {
                compileExpression(es.getExpression());
                // On ignore le résultat (la VM pourrait faire POP plus tard)
            }
            case IfStatement ifStmt -> compileIf(ifStmt);
            case WhileStatement ws -> compileWhile(ws);
            case VarStatement vs -> compileVar(vs);
            case BlockStatement bs -> compileBlock(bs);

            default -> throw new RuntimeError(
                    "Le statement est inconnu",
                    null,
                    statement.getClass().getSimpleName()
            );
        }
    }

    private void compileVar(VarStatement vs) {
        String name = vs.getName().getLexeme();
        int nameIndex = getOrAddGlobalNameIndex(name);

        if (vs.getInitializer() != null) {
            // we put the value on the stack
            compileExpression(vs.getInitializer());
        } else {
            // default value
            chunk.writeConstant(0.0);
        }
        // DEFINE_GLOBAL <idx_nom>
        chunk.writeOpWithOperand(OpCode.DEFINE_GLOBAL, nameIndex);
    }

    private void compileWhile(WhileStatement ws) {
        // 1) start position in our loop
        int loopStart = chunk.getCodeSize();

        // 2) Compile condition
        compileExpression(ws.getCondition());

        // 3) JUMP_IF_FALSE to the end of the loop (temp target = 0)
        int jumpExitPos = chunk.getCodeSize();
        chunk.writeOpWithOperand(OpCode.JUMP_IF_FALSE, 0);

        // 4) Compile the loop body
        compileStatement(ws.getBody());

        // 5) Sjump back at the beggining of the loop
        chunk.writeOpWithOperand(OpCode.JUMP, loopStart);

        // 6) Patch the target of JUMP_IF_FALSE : after the body +  JUMP
        int afterLoop = chunk.getCodeSize();
        // opérande is after opcode
        chunk.getCode().set(jumpExitPos + 1, afterLoop);
    }

    private void compileIf(IfStatement ifStmt) {
        //  Compile condition
        compileExpression(ifStmt.getCondition());

        // Emit JUMP_IF_FALSE with a temp target 0
        int jumpIfFalsePos = chunk.getCodeSize(); // index de l'opcode
        chunk.writeOpWithOperand(OpCode.JUMP_IF_FALSE, 0);

        //  Compile then
        compileStatement(ifStmt.getThenBranch());

        //  If not else : we patch JUMP_IF_FALSE to jump at the end of then
        if (ifStmt.getElseBranch().isEmpty()) {
            int afterThen = chunk.getCodeSize();
            // opérande is tore after opcode
            chunk.getCode().set(jumpIfFalsePos + 1, afterThen);
            return;
        }

        // Else :
        //  JUMP
        int jumpOverElsePos = chunk.getCodeSize();
        chunk.writeOpWithOperand(OpCode.JUMP, 0);

        // We know wher begin the else
        int elseStart = chunk.getCodeSize();
        // patch JUMP_IF_FALSE pour pointer sur le début du else
        chunk.getCode().set(jumpIfFalsePos + 1, elseStart);

        // 7) Compiler la branche else
        compileStatement(ifStmt.getElseBranch().get());

        // 8) Patch du JUMP final pour pointer après le else
        int afterElse = chunk.getCodeSize();
        chunk.getCode().set(jumpOverElsePos + 1, afterElse);
    }

    // Compile Expression in bytecode
    private void compileExpression(Expression expression) {

        switch (expression) {

            case Literal literal ->
                    compileLiteral(literal);

            case Unary unary ->
                    compileUnary(unary);

            case Binary binary ->
                    compileBinary(binary);

            case Variable var ->
                    compileVariable(var);

            case VariableAssignment va ->
                    compileVariableAssignment(va);

            default ->
                    throw new RuntimeError(
                            "Expression non supportée par le bytecode compiler pour l'instant",
                            null,
                            expression.getClass().getSimpleName()
                    );
        }
    }

    private void compileBinary(Binary binary) {
        // We do a reverse polish notation
        // Compile left -> push left
        // compile right -> push right
        // apply operator -> pop/pop -> push result
        compileExpression(binary.getLeft());
        compileExpression(binary.getRight());
        TokenType opType = binary.getOperator().getType();

        switch (opType) {
            case PLUS -> chunk.writeOp(OpCode.ADD);
            case MINUS -> chunk.writeOp(OpCode.SUBTRACT);
            case STAR -> chunk.writeOp(OpCode.MULTIPLY);
            case SLASH -> chunk.writeOp(OpCode.DIVIDE);
            case EQUAL_EQUAL -> chunk.writeOp(OpCode.EQUAL);
            case BANG_EQUAL -> chunk.writeOp(OpCode.NOT_EQUAL);
            case GREATER -> chunk.writeOp(OpCode.GREATER);
            case GREATER_EQUAL -> chunk.writeOp(OpCode.GREATER_EQUAL);
            case LESS -> chunk.writeOp(OpCode.LESS);
            case LESS_EQUAL -> chunk.writeOp(OpCode.LESS_EQUAL);
            default -> throw new RuntimeError(
                    "Opérateur binaire non supporté en VM ",
                    null,
                    String.valueOf(opType)
            );

        }
    }

    private void compileUnary(Unary unary) {
        // First we evaluate the right side, and we leave the value on the top of the stack
        compileExpression(unary.getRight());
        TokenType opType = unary.getOperator().getType();
        if (opType == TokenType.MINUS) {
            //We apply NEGATE
            chunk.writeOp(OpCode.NEGATE);
        } else {
            throw new RuntimeError(
                    "Opérateur unaire non supporté",
                    null,
                    String.valueOf(opType)
            );
        }
    }

    private void compileLiteral(Literal literal) {
        chunk.writeConstant(literal.getValue());
}

    private int getOrAddGlobalNameIndex(String name) {
        return globalNameConstants.computeIfAbsent(name, chunk::addConstant);
    }

    private void compileVariable(Variable var) {
        String name = var.getName().getLexeme();
        int nameIndex = getOrAddGlobalNameIndex(name);

        // GET_GLOBAL <idx_nom>
        chunk.writeOpWithOperand(OpCode.GET_GLOBAL, nameIndex);
    }

    private void compileVariableAssignment(VariableAssignment va) {
        String name = va.getName().getLexeme();
        int nameIndex = getOrAddGlobalNameIndex(name);

        // we check the new value
        compileExpression(va.getValue());

        // SET_GLOBAL <idx_nom> : pop, set, (re)push
        chunk.writeOpWithOperand(OpCode.SET_GLOBAL, nameIndex);
    }

    private void compileBlock(BlockStatement bs) {
        for (Statement stmt : bs.getStatements()) {
            compileStatement(stmt);
        }
    }
}

