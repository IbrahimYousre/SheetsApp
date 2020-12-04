package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.expression.token.Token;

public class ParsingException extends RuntimeException {

    private ParsingException(String message) {
        super(message);
    }

    public static ParsingException unexpectedTokenException(String expected, Token<TokenType> actual, int pos) {
        String message;
        if (actual != null) {
            message = "Expecting token of type (" + expected + ") instead found " + actual + " at pos " + pos;
        } else {
            message = "Expecting token of type (" + expected + ") instead found EOF at pos " + pos;
        }
        return new ParsingException(message);
    }
}
