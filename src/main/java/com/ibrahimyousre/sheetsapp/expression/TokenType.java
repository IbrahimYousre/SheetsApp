package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.expression.token.ITokenType;

import lombok.Getter;

public enum TokenType implements ITokenType {
    STRING_LITERAL("'([^']|'')*'", true),
    NUMBER_LITERAL("[0-9]+", true),
    CELL_REFERENCE_LITERAL("\\$?[a-zA-Z]+\\$?[0-9]+", true),
    FUNCTION_IDENTIFIER_LITERAL("[a-zA-Z_]+", true),
    LP("("),
    RP(")"),
    COMMA(","),
    RANGE(":"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MOD("%"),
    POWER("^"),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    EQ("=="),
    NE("!="),
    ;

    @Getter
    final String value;
    @Getter
    final int length;
    @Getter
    final boolean isRegex;

    TokenType(String value) {
        this(value, false);
    }

    TokenType(String value, boolean isRegex) {
        this.value = value;
        this.length = value.length();
        this.isRegex = isRegex;
    }
}
