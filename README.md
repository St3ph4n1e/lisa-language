# Lisa — Educational Programming Language (M2 Lead Dev)

Lisa is a **mini programming language**, both interpreted and bytecode-compiled, created for the *Language Design* module (Master 2 Lead Developer — ESIEE IT).

The project includes:

- a **lexer**
- a **parser** (custom or ANTLR)
- a **compiler** (Lisa → bytecode)
- a **stack-based Virtual Machine**
- an enhanced **REPL** with syntax highlighting
- **bytecode serialization/deserialization**
- full support for **variables**, **assignments**, **conditions**, **loops**, **blocks**, and more.

Lisa uses French-inspired syntax:

```lisa
variable x est 3;
si (x plus_grand_que 2) {
    affiche "OK";
}
```

---

## ✨ Main Features

### ✔️ Lexer
Detects:
- numbers, strings, identifiers
- French keywords
- comparison & boolean operators
- brackets, parentheses, semicolons
- detailed lexical errors

---

### ✔️ Parser
Builds a full AST with:
- declarations
- assignments
- conditionals
- loops
- blocks
- expressions

Supports two modes:  
`--parser=custom` (default)  
`--parser=generated` (ANTLR)

---

### ✔️ Compiler (AST → Bytecode)
Translates Lisa instructions into VM opcodes:

```
CONSTANT, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE,
PRINT, RETURN,
JUMP, JUMP_IF_FALSE,
DEFINE_GLOBAL, GET_GLOBAL, SET_GLOBAL
```

---

### ✔️ Virtual Machine
- Stack-based execution
- Global variables
- Conditionals via jump instructions
- Robust runtime error handling

---

### ✔️ Advanced REPL
Includes:
- multi-line input
- syntax highlighting
- execution on empty line
- persistent VM state
- optional bytecode serialization

CLI options:

```
--parser=generated
--parser=custom
--serializer
```

---
## 🚀 Running the REPL

### Build

```bash
mvn clean package
```

### Standard mode

```bash
java -cp target/classes org.example.MainVM
```

### With ANTLR parser

```bash
java -cp target/classes org.example.MainVM --parser=generated
```

### With bytecode serialization

```bash
java -cp target/classes org.example.MainVM --serializer
```

---

## 🧠 Examples

### Variables

```lisa
variable x est 5;
x est 10;
affiche x;
```

### Conditionals

```lisa
si (x plus_grand_que 8) {
    affiche "too big";
} sinon {
    affiche "ok";
}
```

### Loops

```lisa
variable i est 0;

tant_que (i plus_petit_que 3) {
    affiche i;
    i est i + 1;
}
```

---

## 👩‍💻 Author

Developed by **St3ph4n1e** & group  
ESIEE IT — Master 2 Lead Developer  
*Language Design 2024–2025*
