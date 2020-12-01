package com.ibrahimyousre.sheetsapp.expression.token;

public interface TokenMatcher<T extends ITokenType> {
    Token<T> getTokenIfMatched(char[] chars, int pos);
}
