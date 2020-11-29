package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.Tokenizer.Token.*;
import static com.ibrahimyousre.sheetsapp.expression.Tokenizer.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TokenizerTest {

    @Test
    public void testStringLiteral() throws Exception {
        assertThat(new Tokenizer("'ibrahim'").getTokens()).containsExactly(
                stringLiteral("'ibrahim'", 0)
        );
        assertThat(new Tokenizer("'ibrahim''s home'").getTokens()).containsExactly(
                stringLiteral("'ibrahim''s home'", 0)
        );
    }

    @Test
    public void testNumberLiteral() throws Exception {
        assertThat(new Tokenizer("1").getTokens()).containsExactly(
                numberLiteral("1", 0)
        );
        assertThat(new Tokenizer("1234").getTokens()).containsExactly(
                numberLiteral("1234", 0)
        );
    }

    @Test
    public void testIdentifierLiteral() throws Exception {
        assertThat(new Tokenizer("a").getTokens()).containsExactly(
                identifierLiteral("a", 0)
        );
        assertThat(new Tokenizer("A").getTokens()).containsExactly(
                identifierLiteral("A", 0)
        );
        assertThat(new Tokenizer("B1").getTokens()).containsExactly(
                identifierLiteral("B1", 0)
        );
        assertThat(new Tokenizer("$B1").getTokens()).containsExactly(
                identifierLiteral("$B1", 0)
        );
        assertThat(new Tokenizer("B$1").getTokens()).containsExactly(
                identifierLiteral("B$1", 0)
        );
        assertThat(new Tokenizer("$B$1").getTokens()).containsExactly(
                identifierLiteral("$B$1", 0)
        );
        assertThat(new Tokenizer("sum").getTokens()).containsExactly(
                identifierLiteral("sum", 0)
        );
    }

    @Test
    public void testExampleExpression() throws Exception {
        assertThat(new Tokenizer("1*2-5^A1+sum(A1:A5)").getTokens()).containsExactly(
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