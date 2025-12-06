package org.example;


import org.example.exceptions.LexicalError;
import org.example.exceptions.RuntimeError;
import org.example.exceptions.SyntaxError;
import org.example.highlight.ReplHighlighter;
import org.example.model.Token;
import org.example.service.ParserStatementsService;
import org.example.utils.TokenReader;

import java.util.List;
import java.util.Scanner;

import static org.example.utils.AnsiColors.*;

public class Main {
    private enum ParserMode { CUSTOM, GENERATED }
    private static ParserMode parserMode = ParserMode.CUSTOM;
    public static void run_repl(){
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║      Lisa language — REPL         ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("Tapez 'exit' pour quitter.");
        System.out.println();
        Scanner scanner =  new Scanner(System.in);
        String text = "";
        while (true) {
            System.out.print(">>> ");
            text = scanner.nextLine().trim();
            if (text.equals("exit")) {
                System.out.println("Au revoir !");
                break;
            }
            if (text.isEmpty()) {
                continue;
            }
            String highlighted = ReplHighlighter.highlight(text);
            System.out.println(highlighted);
            run(text);
        }
    }

    public static void run(String text){
        try{
            if (parserMode == ParserMode.GENERATED) {
                org.example.antlr.Parser p = new org.example.antlr.Parser(text);
                java.util.List<org.example.model.ast.Statement> stmts = p.parse();
                Interpreter interpreter = new Interpreter();
                interpreter.interpret(stmts, 0);
                // System.out.println(stmts);
            } else {
                Lexer lexer = new Lexer(text);
                List<Token> tokens = lexer.tokenize();
                Parser parser = new Parser(tokens);
                TokenReader tokenReader = new TokenReader(parser);
                ParserStatementsService parserStatementsService = new ParserStatementsService(parser, tokenReader);
                java.util.List<org.example.model.ast.Statement> stmts = parserStatementsService.parse();
                Interpreter interpreter = new Interpreter();
                interpreter.interpret(stmts, 0);
                // System.out.println(stmts);
            }

        } catch (LexicalError e){
            System.out.println(e.getMessage());
        }catch (SyntaxError e) {
            System.out.println(e.getMessage());
        } catch (RuntimeError e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(RED + "Erreur interne :" + RESET + " " + BLUE + e.getMessage() + RESET);
        }
    }

    public static void main(String[] args) {
        if (args != null) {
            for (String arg : args) {
                if (arg != null && arg.startsWith("--parser=")) {
                    String value = arg.substring("--parser=".length()).trim().toLowerCase();
                    if ("generated".equals(value)) parserMode = ParserMode.GENERATED;
                    else if ("custom".equals(value)) parserMode = ParserMode.CUSTOM;
                }
            }
        }
        run_repl();
        }
    }
