package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.expression.token.Token;

public class TokenFactory {

    public static Token operationToken(TokenType type, int startPos) {
        return new Token(type, null, startPos);
    }

    public static Token stringLiteral(String data, int startPos) {
        return new Token(TokenType.STRING_LITERAL, data, startPos);
    }

    public static Token numberLiteral(String data, int startPos) {
        return new Token(TokenType.NUMBER_LITERAL, data, startPos);
    }

    public static Token identifierLiteral(String data, int startPos) {
        return new Token(TokenType.IDENTIFIER_LITERAL, data, startPos);
    }
}
