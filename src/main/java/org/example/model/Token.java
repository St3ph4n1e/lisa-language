package org.example.model;

import java.util.Objects;

public class Token {
     private TokenType type;
     private String lexeme;
     private int line ;

     public Token(TokenType type, String lexeme, int line) {
          this.type = type;
          this.lexeme = lexeme;
          this.line = line;
     }

     public TokenType getType() {
          return type;
     }

     public void setType(TokenType type) {
          this.type = type;
     }

     public String getLexeme() {
          return lexeme;
     }

     public void setLexeme(String lexeme) {
          this.lexeme = lexeme;
     }

     public int getLine() {
          return line;
     }

     public void setLine(int line) {
          this.line = line;
     }

     @Override
     public boolean equals(Object obj) {
          if (this == obj) return true;
          if (!(obj instanceof Token)) return false;
          Token other = (Token) obj;
          return this.type == other.type &&
                  Objects.equals(this.lexeme, other.lexeme) &&
                  this.line == other.line;
     }

     @Override
     public int hashCode() {
          return Objects.hash(type, lexeme, line);
     }

     @Override
     public String toString() {
          return String.format("Token(%s, \"%s\", %d)", type, lexeme, line);
     }
}
