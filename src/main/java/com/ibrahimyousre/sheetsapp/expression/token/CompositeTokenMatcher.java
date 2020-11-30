package com.ibrahimyousre.sheetsapp.expression.token;

import java.util.List;

public class CompositeTokenMatcher implements TokenMatcher {
    private final List<TokenMatcher> tokenMatchers;

    public CompositeTokenMatcher(List<TokenMatcher> tokenMatchers) {
        this.tokenMatchers = tokenMatchers;
    }

    @Override
    public Token getTokenIfMatched(char[] chars, int pos) {
        Token token = null;
        for (TokenMatcher tokenMatcher : tokenMatchers) {
            token = tokenMatcher.getTokenIfMatched(chars, pos);
            if (token != null) { break; }
        }
        return token;
    }
}
