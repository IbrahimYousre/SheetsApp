package com.ibrahimyousre.sheetsapp.expression.token;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;

public class DeterministicTokenMatcher implements TokenMatcher {
    private final static Comparator<String> PREFIX_LAST_COMPARATOR =
            ((Comparator<String>) (a, b) -> a.equals(b) ? 0 : (a.startsWith(b) ? -1 : (b.startsWith(a) ? 1 : 0)))
                    .thenComparing(Comparator.naturalOrder());
    private final boolean ignoreWhiteSpace;
    private final List<ITokenType> tokenTypes;

    public DeterministicTokenMatcher(boolean ignoreWhiteSpace, List<ITokenType> tokenTypes) {
        this.ignoreWhiteSpace = ignoreWhiteSpace;
        this.tokenTypes = tokenTypes.stream().filter(ITokenType::isDeterministic)
                .sorted(Comparator.comparing(ITokenType::getValue, PREFIX_LAST_COMPARATOR))
                .collect(toList());
    }

    @Override
    public Token getTokenIfMatched(char[] chars, int pos) {
        char ch = chars[pos];
        if (ignoreWhiteSpace && isWhiteSpace(ch)) { return null; }
        // todo: optimize with binary search
        return tokenTypes.stream().filter(tokenType -> startsWith(chars, pos, tokenType.getValue())).findFirst()
                .map(tokenType -> new Token(tokenType, pos))
                .orElse(null);
    }

    private boolean isWhiteSpace(char c) {
        return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
    }

    private boolean startsWith(char[] chars, int pos, String value) {
        for (int i = 0; i < value.length(); i++) {
            if (i + pos >= chars.length || chars[i + pos] != value.charAt(i)) { return false; }
        }
        return true;
    }
}
