package com.ibrahimyousre.sheetsapp.expression.token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tokenizer<T extends ITokenType> {

    private final List<TokenMatcher<T>> tokenMatchers;

    public Tokenizer(T[] tokens) {
        tokenMatchers = new LinkedList<>();
        List<T> deterministicTokens = new LinkedList<>();
        for (T token : tokens) {
            if (token.isRegex()) {
                tokenMatchers.add(new RegexTokenMatcher<>(token));
            } else {
                deterministicTokens.add(token);
            }
        }
        tokenMatchers.add(new DeterministicTokenMatcher<>(true, deterministicTokens));
    }

    public List<Token<T>> getTokens(String input) {
        char[] chars = input.toCharArray();
        int end = chars.length;
        int pos = 0;
        ArrayList<Token<T>> tokens = new ArrayList<>();
        while (pos < end) {
            Token<T> token = getMatchedToken(chars, pos);
            if (token == null) {
                String message = String.format("Cannot handle (%d) '%1$c' at pos %d", (int) chars[pos], pos);
                throw new IllegalStateException(message);
            }
            tokens.add(token);
            pos += token.getLength();
        }
        return tokens;
    }

    private Token<T> getMatchedToken(char[] chars, int pos) {
        for (TokenMatcher<T> tokenMatcher : tokenMatchers) {
            Token<T> token = tokenMatcher.getTokenIfMatched(chars, pos);
            if (token != null) { return token; }
        }
        return null;
    }

}
