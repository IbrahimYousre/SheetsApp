package com.ibrahimyousre.sheetsapp.expression.token;

import lombok.Value;

@Value
public class Token<T extends ITokenType> {
    T type;
    String data;
    int startPos;
    int length;

    public Token(T type, String data, int startPos) {
        if (type.isDeterministic() && data == null) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.data = data;
        this.startPos = startPos;
        this.length = data.length();
    }

    public Token(T type, int startPos) {
        if (!type.isDeterministic()) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.data = null;
        this.startPos = startPos;
        this.length = type.getLength();
    }

}
