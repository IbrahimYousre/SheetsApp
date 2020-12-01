package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.expression.token.ITokenType;

import lombok.Getter;

public enum TokenType implements ITokenType {
    STRING_LITERAL("'([^']|'')*'", false),
    NUMBER_LITERAL("[0-9]+", false),
    CELL_REFERENCE_LITERAL("\\$?[a-zA-Z]+\\$?[0-9]+", false),
    FUNCTION_IDENTIFIER_LITERAL("[a-zA-Z_]+", false),
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
    final boolean isDeterministic;

    TokenType(String value) {
        this(value, true);
    }

    TokenType(String value, boolean isDeterministic) {
        this.value = value;
        this.length = value.length();
        this.isDeterministic = isDeterministic;
    }
}
