package com.ibrahimyousre.sheetsapp.expression.token;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTokenMatcher<T extends ITokenType> implements TokenMatcher<T> {

    private final T tokenType;
    private final Pattern pattern;

    public RegexTokenMatcher(T tokenType) {
        if (!tokenType.isRegex()) {
            throw new IllegalArgumentException();
        }
        this.pattern = Pattern.compile('^' + tokenType.getValue());
        this.tokenType = tokenType;
    }

    @Override
    public Token<T> getTokenIfMatched(char[] chars, int pos) {
        Matcher matcher = pattern.matcher(CharBuffer.wrap(chars, pos, chars.length - pos));
        if (matcher.lookingAt()) {
            int length = matcher.end();
            String match = String.valueOf(chars, pos, length);
            return Token.ofRegexToken(tokenType, match, pos);
        }
        return null;
    }
}
