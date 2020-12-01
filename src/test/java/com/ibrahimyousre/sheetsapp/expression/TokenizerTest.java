package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.TokenType.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.ibrahimyousre.sheetsapp.expression.token.Token;
import com.ibrahimyousre.sheetsapp.expression.token.Tokenizer;

class TokenizerTest {

    private final Tokenizer tokenizer = new SheetsTokenizer();

    @Test
    public void testStringLiteral() {
        assertThat(tokenizer.getTokens("'ibrahim'")).containsExactly(
                stringLiteral("'ibrahim'", 0)
        );
        assertThat(tokenizer.getTokens("'ibrahim''s home'")).containsExactly(
                stringLiteral("'ibrahim''s home'", 0)
        );
    }

    @Test
    public void testNumberLiteral() {
        assertThat(tokenizer.getTokens("1")).containsExactly(
                numberLiteral("1", 0)
        );
        assertThat(tokenizer.getTokens("1234")).containsExactly(
                numberLiteral("1234", 0)
        );
    }

    @Test
    public void testCellReferenceLiteral() throws Exception {
        assertThat(tokenizer.getTokens("B1")).containsExactly(
                cellReferenceLiteral("B1", 0)
        );
        assertThat(tokenizer.getTokens("$B1")).containsExactly(
                cellReferenceLiteral("$B1", 0)
        );
        assertThat(tokenizer.getTokens("B$1")).containsExactly(
                cellReferenceLiteral("B$1", 0)
        );
        assertThat(tokenizer.getTokens("$B$1")).containsExactly(
                cellReferenceLiteral("$B$1", 0)
        );
    }

    @Test
    public void testFunctionIdentifierLiteral() {
        assertThat(tokenizer.getTokens("a")).containsExactly(
                functionIdentifierLiteral("a", 0)
        );
        assertThat(tokenizer.getTokens("A")).containsExactly(
                functionIdentifierLiteral("A", 0)
        );
        assertThat(tokenizer.getTokens("sum")).containsExactly(
                functionIdentifierLiteral("sum", 0)
        );
    }

    @Test
    public void testExampleExpression() {
        assertThat(tokenizer.getTokens("1*2-5^A1+sum(A1:A5)>1")).containsExactly(
                numberLiteral("1", 0),
                operationToken(MULTIPLY, 1),
                numberLiteral("2", 2),
                operationToken(MINUS, 3),
                numberLiteral("5", 4),
                operationToken(POWER, 5),
                cellReferenceLiteral("A1", 6),
                operationToken(PLUS, 8),
                functionIdentifierLiteral("sum", 9),
                operationToken(LP, 12),
                cellReferenceLiteral("A1", 13),
                operationToken(RANGE, 15),
                cellReferenceLiteral("A5", 16),
                operationToken(RP, 18),
                operationToken(GT, 19),
                numberLiteral("1", 20)
        );
    }

    public static Token operationToken(TokenType type, int startPos) {
        return new Token(type, startPos);
    }

    public static Token stringLiteral(String data, int startPos) {
        return new Token(TokenType.STRING_LITERAL, data, startPos);
    }

    public static Token numberLiteral(String data, int startPos) {
        return new Token(TokenType.NUMBER_LITERAL, data, startPos);
    }

    public static Token cellReferenceLiteral(String data, int startPos) {
        return new Token(TokenType.CELL_REFERENCE_LITERAL, data, startPos);
    }

    public static Token functionIdentifierLiteral(String data, int startPos) {
        return new Token(FUNCTION_IDENTIFIER_LITERAL, data, startPos);
    }

}