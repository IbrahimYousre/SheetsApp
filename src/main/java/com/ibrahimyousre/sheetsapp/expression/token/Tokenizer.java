package com.ibrahimyousre.sheetsapp.expression.token;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Tokenizer {

    private final TokenMatcher[] tokenMatchers;

    public Tokenizer(ITokenType[] values) {
        this.tokenMatchers = Stream.of(values).collect(groupingBy(ITokenType::isDeterministic)).entrySet().stream()
                .flatMap(e -> {
                    if (e.getKey()) {
                        return Stream.of(new DeterministicTokenMatcher(true, e.getValue()));
                    } else {
                        return e.getValue().stream().map(RegularTokenMatcher::new);
                    }
                }).toArray(TokenMatcher[]::new);
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
