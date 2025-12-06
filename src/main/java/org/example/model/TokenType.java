package org.example.model;

public enum TokenType {
    // Single-character tokens
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    SEMICOLON,
    COMMA,
    MINUS,
    PLUS,
    SLASH,
    STAR,

    // One or two character tokens
    BANG,
    BANG_EQUAL,
    EQUAL,
    EQUAL_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    // Literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // Keywords
    AND,
    ELSE,
    FALSE,
    FN,
    FOR,
    IF,
    NULL,
    OR,
    PRINT,
    RETURN,
    TRUE,
    VAR,
    ASSIGN,
    WHILE,
    END,
    THEN,
    IMPORT,
    MATCH,
    DEFAULT,
    ARROW,
    EOF
}
