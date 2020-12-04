package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.ibrahimyousre.sheetsapp.expression.token.Token;

class TokenizerTest {

    private final SheetsTokenizer tokenizer = new SheetsTokenizer();
    private int current;

    private void reset() {
        current = 0;
    }

    @Test
    public void testStringLiteral() {
        assertThat(tokenizer.getTokens("'ibrahim'")).containsExactly(
                stringLiteral("'ibrahim'")
        );
        reset();
        assertThat(tokenizer.getTokens("'ibrahim''s home'")).containsExactly(
                stringLiteral("'ibrahim''s home'")
        );
    }

    @Test
    public void testNumberLiteral() {
        assertThat(tokenizer.getTokens("1")).containsExactly(
                numberLiteral("1")
        );
        reset();
        assertThat(tokenizer.getTokens("1234")).containsExactly(
                numberLiteral("1234")
        );
    }

    @Test
    public void testCellReferenceLiteral() {
        assertThat(tokenizer.getTokens("B1")).containsExactly(
                cellReferenceLiteral("B1")
        );
        reset();
        assertThat(tokenizer.getTokens("$B1")).containsExactly(
                cellReferenceLiteral("$B1")
        );
        reset();
        assertThat(tokenizer.getTokens("B$1")).containsExactly(
                cellReferenceLiteral("B$1")
        );
        reset();
        assertThat(tokenizer.getTokens("$B$1")).containsExactly(
                cellReferenceLiteral("$B$1")
        );
    }

    @Test
    public void testFunctionIdentifierLiteral() {
        assertThat(tokenizer.getTokens("a")).containsExactly(
                functionIdentifierLiteral("a")
        );
        reset();
        assertThat(tokenizer.getTokens("A")).containsExactly(
                functionIdentifierLiteral("A")
        );
        reset();
        assertThat(tokenizer.getTokens("sum")).containsExactly(
                functionIdentifierLiteral("sum")
        );
    }

    @Test
    public void testExampleExpression() {
        assertThat(tokenizer.getTokens("1*2-5^A1+sum(A1:A5)>1")).containsExactly(
                numberLiteral("1"),
                operationToken(MULTIPLY),
                numberLiteral("2"),
                operationToken(MINUS),
                numberLiteral("5"),
                operationToken(POWER),
                cellReferenceLiteral("A1"),
                operationToken(PLUS),
                functionIdentifierLiteral("sum"),
                operationToken(LP),
                cellReferenceLiteral("A1"),
                operationToken(RANGE),
                cellReferenceLiteral("A5"),
                operationToken(RP),
                operationToken(GT),
                numberLiteral("1")
        );
    }

    private Token<TokenType> operationToken(TokenType type) {
        Token<TokenType> tokenTypeToken = Token.ofDeterministicToken(type, current);
        current += tokenTypeToken.getLength();
        return tokenTypeToken;
    }

    private Token<TokenType> stringLiteral(String data) {
        Token<TokenType> tokenTypeToken = Token.ofRegexToken(STRING_LITERAL, data, current);
        current += tokenTypeToken.getLength();
        return tokenTypeToken;
    }

    private Token<TokenType> numberLiteral(String data) {
        Token<TokenType> tokenTypeToken = Token.ofRegexToken(NUMBER_LITERAL, data, current);
        current += tokenTypeToken.getLength();
        return tokenTypeToken;
    }

    private Token<TokenType> cellReferenceLiteral(String data) {
        Token<TokenType> tokenTypeToken = Token.ofRegexToken(CELL_REFERENCE_LITERAL, data, current);
        current += tokenTypeToken.getLength();
        return tokenTypeToken;
    }

    private Token<TokenType> functionIdentifierLiteral(String data) {
        Token<TokenType> tokenTypeToken = Token.ofRegexToken(FUNCTION_IDENTIFIER_LITERAL, data, current);
        current += tokenTypeToken.getLength();
        return tokenTypeToken;
    }

}