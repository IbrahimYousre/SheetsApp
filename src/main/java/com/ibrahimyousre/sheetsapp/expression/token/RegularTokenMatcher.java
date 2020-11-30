package com.ibrahimyousre.sheetsapp.expression.token;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularTokenMatcher implements TokenMatcher {

    private final ITokenType tokenType;
    private final Pattern pattern;

    public RegularTokenMatcher(ITokenType tokenType) {
        this.pattern = Pattern.compile('^' + tokenType.getValue());
        this.tokenType = tokenType;
    }

    @Override
    public Token getTokenIfMatched(char[] chars, int pos) {
        Token token = null;
        Matcher matcher = pattern.matcher(CharBuffer.wrap(chars, pos, chars.length - pos));
        if (matcher.lookingAt()) {
            int length = matcher.end();
            String match = String.valueOf(chars, pos, length);
            token = new Token(tokenType, match, pos);
        }
        return token;
    }
}
