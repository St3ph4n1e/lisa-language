package org.example.utils;

import org.example.Parser;
import org.example.exceptions.SyntaxError;
import org.example.model.Token;
import org.example.model.TokenType;

public class TokenReader {
    private Parser parser;

    public TokenReader(Parser parser) {
        this.parser = parser;
    }

    public boolean match(TokenType...types){
        for (TokenType tokenType: types) {
            if(check(tokenType)){
                advance();
                return true;
            }
        }
        return false;
    }
    public boolean is_at_end(){
        return peek().getType() == TokenType.EOF;
    }
    public Token peek(){
        return parser.getTokens().get(parser.getCurrent());
    }
    public boolean check(TokenType tokenType){
        if(is_at_end())
            return false;
        return peek().getType() == tokenType;
    }
    public Token advance(){
        if(!is_at_end())
            parser.setCurrent(parser.getCurrent()+1);
        return previous();
    }
    public Token previous(){
        int index = parser.getCurrent() - 1;
        return parser.getTokens().get(index);
    }
    public Token consume(TokenType tokenType, String error_msg){
        if(check(tokenType))
            return advance();
        throw new SyntaxError(
                error_msg,
                peek().getLine(),
                peek().getLexeme()
        );
    }
}
