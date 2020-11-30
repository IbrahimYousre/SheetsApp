package com.ibrahimyousre.sheetsapp.expression.token;

import static com.ibrahimyousre.sheetsapp.expression.TokenFactory.operationToken;
import static com.ibrahimyousre.sheetsapp.expression.TokenType.*;

public class DeterministicTokenMatcher implements TokenMatcher {
    private boolean ignoreWhiteSpace;

    public DeterministicTokenMatcher(boolean ignoreWhiteSpace) {
        this.ignoreWhiteSpace = ignoreWhiteSpace;
    }

    @Override
    public Token getTokenIfMatched(char[] chars, int pos) {
        char ch = chars[pos];
        if (ignoreWhiteSpace && isWhiteSpace(ch)) { return null; }
        Token token = null;
        switch (ch) {
            case '(':
                token = operationToken(LP, pos);
                break;
            case ')':
                token = operationToken(RP, pos);
                break;
            case ',':
                token = operationToken(COMMA, pos);
                break;
            case ':':
                token = operationToken(RANGE, pos);
                break;
            case '+':
                token = operationToken(PLUS, pos);
                break;
            case '-':
                token = operationToken(MINUS, pos);
                break;
            case '*':
                token = operationToken(MULTIPLY, pos);
                break;
            case '/':
                token = operationToken(DIVIDE, pos);
                break;
            case '%':
                token = operationToken(MOD, pos);
                break;
            case '^':
                token = operationToken(POWER, pos);
                break;
            case '>':
                if (peekNext(chars, pos, '=')) {
                    token = operationToken(GE, pos);
                } else {
                    token = operationToken(GT, pos);
                }
                break;
            case '<':
                if (peekNext(chars, pos, '=')) {
                    token = operationToken(LE, pos);
                } else {
                    token = operationToken(LT, pos);
                }
                break;
            case '=':
                if (peekNext(chars, pos, '=')) {
                    token = operationToken(EQ, pos);
                }
                break;
            case '!':
                if (peekNext(chars, pos, '=')) {
                    token = operationToken(NE, pos);
                }
                break;
        }
        return token;
    }

    private boolean isWhiteSpace(char c) {
        return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
    }

    private boolean peekNext(char[] chars, int current, char ch) {
        return current + 1 < chars.length && chars[current + 1] == ch;
    }
}
