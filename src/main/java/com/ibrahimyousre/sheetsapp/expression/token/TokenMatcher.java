package com.ibrahimyousre.sheetsapp.expression.token;

public interface TokenMatcher {
    Token getTokenIfMatched(char[] chars, int pos);
}
