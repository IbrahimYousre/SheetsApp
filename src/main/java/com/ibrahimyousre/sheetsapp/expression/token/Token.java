package com.ibrahimyousre.sheetsapp.expression.token;

import lombok.Value;

@Value
public class Token<T extends ITokenType> {

    T type;
    String data;
    int startPos;
    int length;

    private Token(T type, String data, int startPos, int length) {
        this.type = type;
        this.data = data;
        this.startPos = startPos;
        this.length = length;
    }

    public static <T extends ITokenType> Token<T> ofRegexToken(T type, String data, int startPos) {
        if (!type.isRegex() || data == null) {
            throw new IllegalArgumentException();
        }
        return new Token<>(type, data, startPos, data.length());
    }

    public static <T extends ITokenType> Token<T> ofDeterministicToken(T type, int startPos) {
        if (type.isRegex()) {
            throw new IllegalArgumentException();
        }
        return new Token<>(type, null, startPos, type.getLength());
    }

}
