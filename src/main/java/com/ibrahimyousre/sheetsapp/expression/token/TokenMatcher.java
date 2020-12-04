package com.ibrahimyousre.sheetsapp.expression.token;

public interface TokenMatcher<T extends ITokenType> {

    /**
     * @return token or null if token doesn't match
     */
    Token<T> getTokenIfMatched(char[] chars, int pos);
}
