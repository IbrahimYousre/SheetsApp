package com.ibrahimyousre.sheetsapp.expression.token;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Tokenizer<T extends ITokenType> {

    private final List<TokenMatcher<T>> tokenMatchers;

    public Tokenizer(T[] values) {
        Stream<Map.Entry<Boolean, List<T>>> stream = Stream.of(values).collect(groupingBy(T::isDeterministic))
                .entrySet().stream();
        this.tokenMatchers = stream
                .flatMap(e -> {
                    if (e.getKey()) {
                        return Stream.of(new DeterministicTokenMatcher<>(true, e.getValue()));
                    } else {
                        return e.getValue().stream().map(RegularTokenMatcher::new);
                    }
                }).collect(toList());
    }

    public List<Token<T>> getTokens(String input) {
        char[] chars = input.toCharArray();
        int end = chars.length;
        int pos = 0;
        ArrayList<Token<T>> tokens = new ArrayList<>();
        while (pos < end) {
            Token<T> token = null;
            for (TokenMatcher<T> tokenMatcher : tokenMatchers) {
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
