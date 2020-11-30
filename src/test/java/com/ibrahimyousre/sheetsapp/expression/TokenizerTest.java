package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.TokenFactory.*;
import static com.ibrahimyousre.sheetsapp.expression.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.ibrahimyousre.sheetsapp.expression.token.Tokenizer;

class TokenizerTest {

    Tokenizer tokenizer = new SheetsTokenizer();

    @Test
    public void testStringLiteral() throws Exception {
        assertThat(tokenizer.getTokens("'ibrahim'")).containsExactly(
                stringLiteral("'ibrahim'", 0)
        );
        assertThat(tokenizer.getTokens("'ibrahim''s home'")).containsExactly(
                stringLiteral("'ibrahim''s home'", 0)
        );
    }

    @Test
    public void testNumberLiteral() throws Exception {
        assertThat(tokenizer.getTokens("1")).containsExactly(
                numberLiteral("1", 0)
        );
        assertThat(tokenizer.getTokens("1234")).containsExactly(
                numberLiteral("1234", 0)
        );
    }

    @Test
    public void testIdentifierLiteral() throws Exception {
        assertThat(tokenizer.getTokens("a")).containsExactly(
                identifierLiteral("a", 0)
        );
        assertThat(tokenizer.getTokens("A")).containsExactly(
                identifierLiteral("A", 0)
        );
        assertThat(tokenizer.getTokens("B1")).containsExactly(
                identifierLiteral("B1", 0)
        );
        assertThat(tokenizer.getTokens("$B1")).containsExactly(
                identifierLiteral("$B1", 0)
        );
        assertThat(tokenizer.getTokens("B$1")).containsExactly(
                identifierLiteral("B$1", 0)
        );
        assertThat(tokenizer.getTokens("$B$1")).containsExactly(
                identifierLiteral("$B$1", 0)
        );
        assertThat(tokenizer.getTokens("sum")).containsExactly(
                identifierLiteral("sum", 0)
        );
    }

    @Test
    public void testExampleExpression() throws Exception {
        assertThat(tokenizer.getTokens("1*2-5^A1+sum(A1:A5)")).containsExactly(
                numberLiteral("1", 0),
                operationToken(MULTIPLY, 1),
                numberLiteral("2", 2),
                operationToken(MINUS, 3),
                numberLiteral("5", 4),
                operationToken(POWER, 5),
                identifierLiteral("A1", 6),
                operationToken(PLUS, 8),
                identifierLiteral("sum", 9),
                operationToken(LP, 12),
                identifierLiteral("A1", 13),
                operationToken(RANGE, 15),
                identifierLiteral("A5", 16),
                operationToken(RP, 18)
        );
    }

}