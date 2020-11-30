package com.ibrahimyousre.sheetsapp.expression.token;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private final TokenMatcher[] tokenMatchers;

    public Tokenizer(TokenMatcher... tokenMatchers) {
        this.tokenMatchers = tokenMatchers;
    }

    public List<Token> getTokens(String input) {
        char[] chars = input.toCharArray();
        int end = chars.length;
        int pos = 0;
        ArrayList<Token> tokens = new ArrayList<>();
        while (pos < end) {
            Token token = null;
            for (TokenMatcher tokenMatcher : tokenMatchers) {
                token = tokenMatcher.getTokenIfMatched(chars, pos);
                if (token != null) { break; }
            }
            if (token == null) {
                String message = String.format("Cannot handle (%d) '%1$c' at pos %d", (int) chars[pos], pos);
                throw new IllegalStateException(message);
            }
            tokens.add(token);
            pos += token.getLength();
        }
        return tokens;
    }

}
