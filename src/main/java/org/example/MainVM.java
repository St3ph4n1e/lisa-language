package org.example;

import org.example.exceptions.LexicalError;
import org.example.exceptions.RuntimeError;
import org.example.exceptions.SyntaxError;
import org.example.highlight.ReplHighlighter;
import org.example.model.Token;
import org.example.model.ast.Statement;
import org.example.service.ParserStatementsService;
import org.example.utils.TokenReader;
import org.example.vm.BytecodeSerializer;
import org.example.vm.Chunk;
import org.example.vm.LisaCompiler;
import org.example.vm.VM;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static org.example.utils.AnsiColors.*;

public class MainVM {
    private enum ParserMode { CUSTOM, GENERATED }
    private static ParserMode parserMode = ParserMode.CUSTOM;

    private static boolean useSerializer = false;

    // UNE SEULE VM pour tout le REPL
    // → les variables globales Lisa (DEFINE_GLOBAL / GET_GLOBAL / SET_GLOBAL) persistent
    private static final VM vm = new VM();

    public static void run_repl() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║      Lisa language — REPL (VM)    ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("Tapez 'exit' pour quitter.");
        System.out.println("Taper une ligne vide pour exécuter le bloc en cours.");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        StringBuilder buffer = new StringBuilder();

        while (true) {
            // Prompt différent selon si on est au début d’un bloc ou au milieu
            if (buffer.length() == 0) {
                System.out.print(">>> ");
            } else {
                System.out.print("--- ");
            }

            String line = scanner.nextLine();

            // Cas "exit" : on sort seulement si on n’a rien en cours
            if (line != null && line.trim().equals("exit") && buffer.length() == 0) {
                System.out.println("Au revoir !");
                break;
            }

            // Ligne vide
            if (line == null || line.trim().isEmpty()) {
                // Si pas de code en cours → on ignore juste
                if (buffer.length() == 0) {
                    continue;
                }

                String source = buffer.toString();

                // Vérifie si le bloc semble complet côté accolades
                if (!isBlockComplete(source)) {
                    System.out.println(GRAY + "[info]" + RESET + " Bloc incomplet (accolades non équilibrées), continuez à taper...");
                    // On ne vide pas le buffer, on laisse l'utilisateur ajouter des lignes
                    continue;
                }

                // Highlight du bloc complet
                String highlighted = ReplHighlighter.highlight(source);
                System.out.println(highlighted);

                // Compilation + exécution
                run(source);
                buffer.setLength(0); // on vide le buffer pour le prochain bloc
                continue;
            }
            // Ligne non vide : on l’ajoute au buffer + un saut de ligne
            buffer.append(line).append("\n");
        }
    }


    public static void run(String text){
        try{
            List<Statement> stmts;

            if (parserMode == ParserMode.GENERATED) {
                // Parser ANTLR →  AST Lisa
                org.example.antlr.Parser p = new org.example.antlr.Parser(text);
                stmts = p.parse();
            } else {
                Lexer lexer = new Lexer(text);
                List<Token> tokens = lexer.tokenize();
                Parser parser = new Parser(tokens);
                TokenReader tokenReader = new TokenReader(parser);
                ParserStatementsService parserStatementsService = new ParserStatementsService(parser, tokenReader);
                stmts = parserStatementsService.parse();
            }

            LisaCompiler compiler = new LisaCompiler();
            Chunk chunk = compiler.compile(stmts);

            Object result;

            if (useSerializer) {
                // Save Bytecode in a temp file
                String filename = "repl_chunk_" + System.currentTimeMillis() + ".lbc";
                BytecodeSerializer.saveBytecode(chunk, filename);
                System.out.println(GREEN + "[info]" + RESET + " Bytecode sauvegardé dans " + BLUE + filename + RESET);

                // Load from file
                Chunk loaded = BytecodeSerializer.loadBytecode(filename);

                // Run fron VM
                result = vm.run(loaded);
            } else {
                result = vm.run(chunk);
            }

            if (result != null) {
                System.out.println(result);
            }

        } catch (LexicalError e){
            System.out.println(e.getMessage());
        } catch (SyntaxError e) {
            System.out.println(e.getMessage());
        } catch (RuntimeError e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(RED + "Erreur interne :" + RESET + " " + BLUE + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    private static boolean isBlockComplete(String code) {
        int balance = 0;
        for (char c : code.toCharArray()) {
            if (c == '{') balance++;
            else if (c == '}') balance--;
            if (balance < 0) {
                // Trop de '}', on considère le bloc "terminé" pour laisser le parser gérer l'erreur
                return true;
            }
        }
        // complet seulement si autant de '{' que de '}'
        return balance == 0;
    }

    private static void saveSnippet(String code) {
        try {
            String dir = "repl_snippets";
            Files.createDirectories(Paths.get(dir));

            String filename = "snippet_" + System.currentTimeMillis() + ".lisa";
            Path path = Paths.get(dir, filename);

            Files.writeString(path, code, StandardCharsets.UTF_8);
            System.out.println(GREEN + "[info]" + RESET + " Code sauvegardé dans " + BLUE + path + RESET);
        } catch (Exception e) {
            System.out.println(RED + "Erreur lors de la sauvegarde du code :" + RESET + " " + BLUE + e.getMessage() + RESET);
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
                if ("--serializer".equals(arg)) {
                    useSerializer = true;
                }
            }
        }
        run_repl();
    }
}