package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.functions.SheetFunctions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibrahimyousre.sheetsapp.expression.token.Token;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public class EquationParser {
    SheetsTokenizer tokenizer = new SheetsTokenizer();
    private List<Token<TokenType>> tokens;
    private int current;

    SheetFunction parseEquation(String equation) {
        tokens = tokenizer.getTokens(equation);
        current = 0;
        return constantLiteral();
    }

    SheetFunction constantLiteral() {
        if (accept(TokenType.NUMBER_LITERAL)) {
            return numberLiteral();
        } else if (accept(TokenType.STRING_LITERAL)) {
            return stringLiteral();
        } else if (accept(TokenType.CELL_REFERENCE_LITERAL)) {
            return cellReferenceLiteral();
        } else {
            throw failExpectation(TokenType.NUMBER_LITERAL, TokenType.STRING_LITERAL);
        }
    }

    SheetFunction numberLiteral() {
        Token<TokenType> token = consume(TokenType.NUMBER_LITERAL);
        return constant(token.getData());
    }

    SheetFunction stringLiteral() {
        Token<TokenType> token = consume(TokenType.STRING_LITERAL);
        String quotedLiteral = token.getData();
        return constant(quotedLiteral.substring(1, quotedLiteral.length() - 1));
    }

    SheetFunction cellReferenceLiteral() {
        Token<TokenType> token = consume(TokenType.CELL_REFERENCE_LITERAL);
        return reference(token.getData());
    }

    /* UTILS */

    boolean accept(TokenType expected) {
        if (expected == tokens.get(current).getType()) {
            return true;
        }
        return false;
    }

    Token<TokenType> consume(TokenType expected) {
        if (expected == tokens.get(current).getType()) {
            return tokens.get(current++);
        }
        throw failExpectation(expected);
    }

    private RuntimeException failExpectation(TokenType expected) {
        return new ParsingException("Expecting token of type (" + expected + ") instead found " + tokens.get(current));
    }

    private RuntimeException failExpectation(TokenType... expectedTypes) {
        String expected = Stream.of(expectedTypes).map(Enum::name)
                .collect(Collectors.joining(" or "));
        return new ParsingException("Expecting token of type (" + expected + ") instead found " + tokens.get(current));
    }
}
