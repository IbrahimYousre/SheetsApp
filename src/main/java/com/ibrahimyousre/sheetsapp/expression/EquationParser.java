package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.TokenType.*;
import static com.ibrahimyousre.sheetsapp.functions.SheetFunctions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibrahimyousre.sheetsapp.expression.token.Token;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;
import com.ibrahimyousre.sheetsapp.functions.SheetFunctions;

public class EquationParser {
    SheetsTokenizer tokenizer = new SheetsTokenizer();
    private List<Token<TokenType>> tokens;
    private int current;

    SheetFunction parseEquation(String equation) {
        tokens = tokenizer.getTokens(equation);
        current = 0;
        return equationExpression();
    }

    // (factor [+-])* factor
    private SheetFunction equationExpression() {
        SheetFunction first = factorExpression();
        while (canConsume(PLUS, MINUS)) {
            if (canConsume(PLUS)) {
                consume();
                SheetFunction second = factorExpression();
                first = plus(first, second);
            } else if (canConsume(MINUS)) {
                consume();
                SheetFunction second = factorExpression();
                first = minus(first, second);
            } else if (!isDone()) {
                throw failExpectation(PLUS, MINUS);
            }
        }
        return first;
    }

    // (value [*/])* value
    private SheetFunction factorExpression() {
        SheetFunction first = powerExpression();
        while (canConsume(MULTIPLY, DIVIDE)) {
            if (canConsume(MULTIPLY)) {
                consume();
                SheetFunction second = powerExpression();
                first = multiply(first, second);
            } else if (canConsume(DIVIDE)) {
                consume();
                SheetFunction second = powerExpression();
                first = divide(first, second);
            }
        }
        return first;
    }

    private SheetFunction powerExpression() {
        SheetFunction first = valueExpression();
        if (canConsume(POWER)) {
            consume();
            SheetFunction second = powerExpression();
            return power(first, second);
        }
        return first;
    }

    // constant | (equation)
    private SheetFunction valueExpression() {
        if (canConsume(LP)) {
            consume();
            SheetFunction equation = equationExpression();
            consume(RP);
            return equation;
        }
        return constant();
    }

    // numberLiteral | stringLiteral | cellReferenceLiteral
    SheetFunction constant() {
        if (canConsume(NUMBER_LITERAL)) {
            return numberLiteral();
        } else if (canConsume(STRING_LITERAL)) {
            return stringLiteral();
        } else if (canConsume(CELL_REFERENCE_LITERAL)) {
            return cellReferenceLiteral();
        } else {
            throw failExpectation(NUMBER_LITERAL, STRING_LITERAL);
        }
    }

    SheetFunction numberLiteral() {
        Token<TokenType> token = consume(NUMBER_LITERAL);
        return SheetFunctions.constant(token.getData());
    }

    SheetFunction stringLiteral() {
        Token<TokenType> token = consume(STRING_LITERAL);
        String quotedLiteral = token.getData();
        return SheetFunctions.constant(quotedLiteral.substring(1, quotedLiteral.length() - 1));
    }

    SheetFunction cellReferenceLiteral() {
        Token<TokenType> token = consume(CELL_REFERENCE_LITERAL);
        return reference(token.getData());
    }

    /* UTILS */

    boolean isDone() {
        return current >= tokens.size();
    }

    boolean canConsume(TokenType expected) {
        if (!isDone() && expected == tokens.get(current).getType()) {
            return true;
        }
        return false;
    }

    boolean canConsume(TokenType... expected) {
        return Stream.of(expected).anyMatch(this::canConsume);
    }

    Token<TokenType> consume() {
        return tokens.get(current++);
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
