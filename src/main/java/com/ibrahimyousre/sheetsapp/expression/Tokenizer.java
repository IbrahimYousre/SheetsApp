package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.Tokenizer.Token.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Value;

public class Tokenizer {

    private final String input;
    private final char[] chars;

    private final int end;
    private int pos;

    public Tokenizer(String input) {
        this.input = input;
        this.chars = input.toCharArray();
        end = input.length();
        pos = 0;
    }

    public List<Token> getTokens() {
        ArrayList<Token> tokens = new ArrayList<>();
        while (pos < end) {
            char ch = chars[pos];
            Token token = null;
            if (isIdentifierPrefix(ch)) {
                token = getIdentifierLiteralToken();
            } else if (isNumber(ch)) {
                token = getNumericLiteralToken();
            } else {
                switch (ch) {
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        pos++;
                        continue;
                    case '\'':
                        token = getStringLiteralToken();
                        break;
                    case '(':
                        token = operationToken(TokenType.LP, pos);
                        break;
                    case ')':
                        token = operationToken(TokenType.RP, pos);
                        break;
                    case ',':
                        token = operationToken(TokenType.COMMA, pos);
                        break;
                    case ':':
                        token = operationToken(TokenType.RANGE, pos);
                        break;
                    case '+':
                        token = operationToken(TokenType.PLUS, pos);
                        break;
                    case '-':
                        token = operationToken(TokenType.MINUS, pos);
                        break;
                    case '*':
                        token = operationToken(TokenType.MULTIPLY, pos);
                        break;
                    case '/':
                        token = operationToken(TokenType.DIVIDE, pos);
                        break;
                    case '%':
                        token = operationToken(TokenType.MOD, pos);
                        break;
                    case '^':
                        token = operationToken(TokenType.POWER, pos);
                        break;
                    case '>':
                        if (peekNext(pos, '=')) {
                            token = operationToken(TokenType.GE, pos);
                        } else {
                            token = operationToken(TokenType.GT, pos);
                        }
                        break;
                    case '<':
                        if (peekNext(pos, '=')) {
                            token = operationToken(TokenType.LE, pos);
                        } else {
                            token = operationToken(TokenType.LT, pos);
                        }
                        break;
                    case '=':
                        if (peekNext(pos, '=')) {
                            token = operationToken(TokenType.EQ, pos);
                        }
                        break;
                    case '!':
                        if (peekNext(pos, '=')) {
                            token = operationToken(TokenType.NE, pos);
                        }
                        break;
                }
            }
            if (token == null) {
                throw new IllegalStateException("Cannot handle (" + (int) ch + ") '" + ch + "'");
            }
            tokens.add(token);
            pos += token.getLength();
        }
        return tokens;
    }

    private boolean peekNext(int current, char ch) {
        return current + 1 < end && chars[current + 1] == ch;
    }

    private boolean isIdentifierPrefix(char ch) {
        return ch == '$' | (ch | 32) >= 'a' && (ch | 32) <= 'z';
    }

    private boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private Token getStringLiteralToken() {
        int current = pos + 1;
        while (current < end) {
            if (chars[current] == '\'' && peekNext(current, '\'')) {
                current += 2;
            } else if (chars[current] != '\'') {
                current++;
            } else {
                current++;
                break;
            }
        }
        return stringLiteral(String.valueOf(chars, pos, current - pos), pos);
    }

    // todo: add decimal and scientific notation
    private Token getNumericLiteralToken() {
        int current = pos;
        while (current < end) {
            char ch = chars[current];
            if (!isNumber(ch)) { break; }
            current++;
        }
        return numberLiteral(String.valueOf(chars, pos, current - pos), pos);
    }

    private Token getIdentifierLiteralToken() {
        int current = pos;
        while (current < end) {
            char ch = chars[current];
            if (!isIdentifierPrefix(ch) && !isNumber(ch)) { break; }
            current++;
        }
        return identifierLiteral(String.valueOf(chars, pos, current - pos), pos);
    }

    enum TokenType {
        STRING_LITERAL(""),
        NUMBER_LITERAL(""),
        IDENTIFIER_LITERAL(""),
        LP("("),
        RP(")"),
        COMMA(","),
        RANGE(":"),
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        MOD("%"),
        POWER("^"),
        GT(">"),
        GE(">="),
        LT("<"),
        LE("<="),
        EQ("=="),
        NE("!="),
        ;

        @Getter
        final String value;
        @Getter
        final char[] chars;
        @Getter
        final boolean isDeterministic;

        TokenType(String value) {
            this.value = value;
            this.chars = value.toCharArray();
            this.isDeterministic = value.length() != 0;
        }

        int getLength() {
            return chars.length;
        }
    }

    @Value
    static class Token {
        TokenType type;
        String data;
        int startPos;
        int length;

        public Token(TokenType type, String data, int startPos, int length) {
            if (type.isDeterministic() ^ data == null) {
                throw new IllegalArgumentException();
            }
            this.type = type;
            this.data = data;
            this.startPos = startPos;
            this.length = length;
        }

        public static Token operationToken(TokenType type, int startPos) {
            return new Token(type, null, startPos, type.getLength());
        }

        public static Token stringLiteral(String data, int startPos) {
            return new Token(TokenType.STRING_LITERAL, data, startPos, data.length());
        }

        public static Token numberLiteral(String data, int startPos) {
            return new Token(TokenType.NUMBER_LITERAL, data, startPos, data.length());
        }

        public static Token identifierLiteral(String data, int startPos) {
            return new Token(TokenType.IDENTIFIER_LITERAL, data, startPos, data.length());
        }
    }
}
