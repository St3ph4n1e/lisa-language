package org.example.vm;
/**
 * Enum representing each operation supported by the VM.
 * Each opcode is mapped to a numeric value using ordinal().
 *
 * Example:
 *  OpCode.ADD.ordinal() == 1
 *  OpCode.PRINT.ordinal() == 6
 *
 * The VM stores and reads those numeric values inside the bytecode.
 */

public enum OpCode {
    CONSTANT,
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NEGATE,
    EQUAL,
    NOT_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,
    JUMP,
    JUMP_IF_FALSE,
    DEFINE_GLOBAL,
    GET_GLOBAL,
    SET_GLOBAL,
    PRINT,
    RETURN
}
