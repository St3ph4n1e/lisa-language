package org.example.vm;

import org.example.exceptions.RuntimeError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VM {

    //Stack to store the values during runtime
    private final List<Object> stack = new ArrayList<>();

    // global Environnement  ( Lisa variables)
    private final Map<String, Object> globals = new HashMap<>();

    //Instruction pointer : index of current instruction in the bytecode
    private int ip;

    // Currently running chunk ( code and constants)
    private Chunk chunk;

    /**
     * Run a Lisa Bytecode chunk
     *
     * @param chunk compiled bytecode  (instructions + constants)
     * @return return value -> RETURN, or null if the stack is empty
     */
    public Object run(Chunk chunk){
        this.chunk = chunk;
        this.ip = 0;
        this.stack.clear();

        while(true) {
            int opcodeInt = readCode();
            OpCode op = OpCode.values()[opcodeInt];

            switch (op) {
                case CONSTANT -> {
                    int constIndex = readCode();
                    Object value = chunk.getConstant(constIndex);
                    push(value);
                }
                case ADD -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a + b);
                }
                case SUBTRACT -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a - b);
                }
                case MULTIPLY -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a * b);
                }
                case DIVIDE -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    if (b == 0.0) {
                        throw new RuntimeError(
                                "Division par zéro dans la VM",
                                null,
                                a + " / 0"
                        );
                    }
                    push(a / b);
                }
                case NEGATE -> {
                    double value = toDouble(pop());
                    push(-value);
                }
                case EQUAL -> {
                    Object b = pop();
                    Object a = pop();
                    push(a.equals(b));
                }
                case NOT_EQUAL -> {
                    Object b = pop();
                    Object a = pop();
                    push(!a.equals(b));
                }
                case GREATER -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a > b);
                }
                case GREATER_EQUAL -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a >= b);
                }
                case LESS -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a < b);
                }
                case LESS_EQUAL -> {
                    double b = toDouble(pop());
                    double a = toDouble(pop());
                    push(a <= b);
                }
                case PRINT -> {
                    Object value = pop();
                    System.out.println(value);
                }
                case JUMP -> {
                    // read opérande after opcode
                    int target = readCode();
                    // we jump to this adress
                    ip = target;
                }
                case JUMP_IF_FALSE -> {
                    // read jum adress
                    int target = readCode();

                    Object cond = pop();
                    if (cond instanceof Boolean b && !b) {
                        // condition false -> we jumpp
                        ip = target;
                    }
                    // else we continue (ip is on the next instruction)
                }
                case DEFINE_GLOBAL -> {
                    // index in constants
                    int nameIndex = readCode();
                    String name = (String) chunk.getConstant(nameIndex);
                    // valeur to assign
                    Object value = pop();
                    // définition
                    globals.put(name, value);
                }
                case GET_GLOBAL -> {
                    int nameIndex = readCode();
                    String name = (String) chunk.getConstant(nameIndex);

                    if (!globals.containsKey(name)) {
                        throw undefinedVariable(name);
                    }
                    push(globals.get(name));
                }
                case SET_GLOBAL -> {
                    int nameIndex = readCode();
                    String name = (String) chunk.getConstant(nameIndex);

                    if (!globals.containsKey(name)) {
                        throw undefinedVariable(name);
                    }
                    Object value = pop();
                    globals.put(name, value);
                    // optional : we put the value on the stack as result of the expression
                    push(value);
                }
                case RETURN -> {
                    // End of program: we go to the stack top if exist
                    if (stack.isEmpty()) {
                        return null;
                    }
                    return pop();
                }
                default -> throw new RuntimeError(
                        "Instruction inconnue",
                        null,
                        op.name()
                );
            }
        }
    }


    private int readCode() {
        int value = chunk.readCode(ip);
        ip += 1;
        return value;
    }

    private void push(Object value) {
        stack.add(value);
    }

    private Object pop() {
        if (stack.isEmpty()) {
            throw new RuntimeError(
                    "La stack est vide",
                    null,
                    "pop()"
            );
        }
        return stack.remove(stack.size() - 1);
    }

    private double toDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        throw new RuntimeError(
                "Il faut que les deux valeurs soient des nombres",
                null,
                String.valueOf(value)
        );
    }

    // For the tests
    public List<Object> getStackSnapshot() {
        return new ArrayList<>(stack);
    }

    private RuntimeError undefinedVariable(String name) {
        return new RuntimeError(
                "Cette variable n'est pas définie",
                null,
                name
        );
    }

}

