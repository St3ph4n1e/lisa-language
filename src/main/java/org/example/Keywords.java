package org.example;

import org.example.model.TokenType;

import java.util.HashMap;
import java.util.Map;

public class Keywords {
    public static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("import", TokenType.IMPORT);
        KEYWORDS.put("et", TokenType.AND);
        KEYWORDS.put("sinon", TokenType.ELSE);
        KEYWORDS.put("faux", TokenType.FALSE);
        KEYWORDS.put("fonction", TokenType.FN);
        KEYWORDS.put("pour", TokenType.FOR);
        KEYWORDS.put("si", TokenType.IF);
        KEYWORDS.put("rien", TokenType.NULL);
        KEYWORDS.put("ou", TokenType.OR);
        KEYWORDS.put("affiche", TokenType.PRINT);
        KEYWORDS.put("retourne", TokenType.RETURN);
        KEYWORDS.put("vrai", TokenType.TRUE);
        KEYWORDS.put("variable", TokenType.VAR);
        KEYWORDS.put("tant_que", TokenType.WHILE);
        KEYWORDS.put("est", TokenType.EQUAL);
        KEYWORDS.put("egale", TokenType.EQUAL_EQUAL);
        KEYWORDS.put("FIN", TokenType.END);
        KEYWORDS.put("devient", TokenType.ASSIGN);
        KEYWORDS.put("plus_grand_que", TokenType.GREATER);
        KEYWORDS.put("plus_grand_ou_egal", TokenType.GREATER_EQUAL);
        KEYWORDS.put("plus_petit_que", TokenType.LESS);
        KEYWORDS.put("plus_petit_ou_egal", TokenType.LESS_EQUAL);
        KEYWORDS.put("alors", TokenType.THEN);
        KEYWORDS.put("contraire_de",TokenType.BANG);
        KEYWORDS.put("different_de",TokenType.BANG_EQUAL);
        KEYWORDS.put("debut",TokenType.LBRACE);
        KEYWORDS.put("fin",TokenType.RBRACE);
        KEYWORDS.put("correspond_a",TokenType.MATCH);
        KEYWORDS.put("par_defaut",TokenType.DEFAULT);
    }
}
