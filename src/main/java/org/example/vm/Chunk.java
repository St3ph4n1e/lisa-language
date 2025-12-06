package org.example.vm;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    // A chunck is a compiled language
    // we store the ordinals values of our OpCode
    private final List<Integer> code = new ArrayList<>();
    private final List<Object> constants = new ArrayList<>();

    // constants
    //---------------------------------------------------
    // Add constants to the table constants and retrun the index
    public int addConstant(Object value ){
        constants.add(value);
        return constants.size()-1;
    }
    // The VM will use getConstant
    // When it read CONSTANT O -> getConstant(O)
    public Object getConstant(int index){
        return constants.get(index);
    }
    // We can use this method for debug and test
    public List<Object> getConstants(){
        return constants;
    }

    // bytecode
    //---------------------------------------------------
    //Add OpCode in list code
    // we dont store the Opcode but the position in the enum
    public void writeOp(OpCode op){
        code.add(op.ordinal());
    }
    // Helper for the Opcode with operand
    public  void writeOpWithOperand(OpCode op, int operand){
        code.add(op.ordinal());
        code.add(operand);
    }

    public int getCodeSize(){
        return code.size();
    }

    public int readCode(int ip){
       return code.get(ip);
    }

    public List<Integer> getCode(){
        return code;
    }
    // add value to the constant table
    // Store a literal value (number, string…) in the constants table,
    // then emit the bytecode “CONSTANT <index>”.
    // Example: literal 42 → constants = [42], code = [CONSTANT, 0]
    public void writeConstant(Object value){
        int idx = addConstant(value);
        writeOpWithOperand(OpCode.CONSTANT, idx);
    }
}
