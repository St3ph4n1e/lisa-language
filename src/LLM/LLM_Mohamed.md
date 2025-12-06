# Prompts utilisés pour le développement du Lexer (Langage Lisa)

Ce document regroupe les prompts envoyés à l'IA dans le cadre du développement de l'interpréteur du langage **Lisa**.

---

## 1. Revue complete de l'interpreteur de cours

### Prompt
```
hey here is my interpreter in python explain me how it works, espescially the flow when you start from having a list of statement of a simple code at first 2 lines and then more complex like 4-6 lines and the process the output. Seeing from where it goes. you can act as a debugger by also showing me the value of the variable at each step
```

Une partie de la réposonse
```
Step 2: First statement - VarStatement
pythonexecute(VarStatement(name=Token("x"), initializer=Literal(5)))
  ↓
  # Match case VarStatement
  value = evaluate(Literal(5))  # Returns: 5
  environment.define("x", 5)
  
# Environment now: {"x": 5}
```
---

## 2. Continuation du prompt - Refonte UI/UX

### Prompt
```
Je veux que tu modifies mon HTML + CSS pour en faire une version moderne, épurée et enfantine, inspirée du site Hedy. Ne touche pas au script.
```

### Réponse clé
- Analyse UX/UI du projet
- Propositions de styles plus modernes : couleurs pastels, ombres douces, arrondis cohérents
- Maintien strict de la structure HTML et du JavaScript existant

---

## 3. Génération de JSON d'exercices

### Prompt
```
Réécris cet exemple en JSON.
```
*(remplacement de l'énoncé d'un exemple d'exercice pas très clair)*

### Réponse clé
Production d'un JSON propre contenant :
- Le titre du niveau
- Une description pédagogique
- Un starter code cohérent
- L'`expectedOutput`

---

## 4. Accompagnement dans la création de tests

### Prompt
```
Ok, from now on you will be my coding assistant. I expect you to go straight to the point with 2 sentences max for explanation and for code respect the KISS principle.

I will ask you questions and I'd like this format:
- Little description
- Code snippet

And you then wait for my other question.

# Context
I work on the implementation of a new programming language in Java and I am currently working on the interpreter, especially on the test side. I already have a generated Lisa Parser and my goal is basically to put a set of tests that work.
```

---

## 5. Génération du Parser

### 5.1 Génération du fichier Lisa.g4

#### Prompt
```
Je suis en Java et j'essaie d'avoir un parser grâce aux fichiers G4. Premièrement, génère-moi le fichier Lisa.G4 basé sur cet exemple de code: <exemples de code Lisa>
```

### 5.2 Génération de l'intégration ANTLR Parser

#### Prompt
```
Generate ANTLR Parser Integration for Lisa Language

I have:
- src/main/java/org/example/generatedParser/LISA.g4 (ANTLR grammar - will auto-generate LISALexer.java, LISAParser.java, LISAVisitor.java)
- src/main/java/org/example/model/ast/AstNode.java (AST classes: VarStatement, Binary, Literal, etc.)
- src/main/java/org/example/model/Token.java (Token, TokenType enums)
- Working interpreter that expects Parser(source).parse() returning list[Statement]

Create these 2 files:

1. src/main/java/org/example/antlr/Parser.JAVA
   - Wrapper class: Parser(source: str) with parse() -> list[Statement]
   - ANTLR pipeline: InputStream → LISALexer → CommonTokenStream → LISAParser.program() → LISAASTBuilder.visit()
   - Custom ErrorListener that raises Java SyntaxError with line numbers

2. src/main/java/org/example/antlr/Visitor.java
   - Class LISAASTBuilder(LISAVisitor)
   - Implement visit* method for each grammar rule (visitProgram, visitVarDecl, visitTerm, etc.)
   - Build left-associative Binary nodes for operators
   - Create Token objects: Token(TokenType.X, "text", line_number)
   - Desugar for loops to while loops (see grammar comment)
   - Handle string escapes and literals ("true", "false", "null")

3. src/test/java/org/example/AntlrTest.java
   - Test that Parser(source).parse() returns expected AST
   - This is a copy of `src/test/java/org/example/LisaTest.java` with only the parser part (no lexer or interpreter)
   - Run the tests with `./.venv/bin/python -m pytest --path src/toy/tests/test_antlr.py`
   - Fix the antlr implementation until these tests pass

Attach: Toy.g4, ast_nodes.py, tokens.py
```
