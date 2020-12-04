package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.expression.TokenType.*;
import static com.ibrahimyousre.sheetsapp.functions.SheetFunctions.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibrahimyousre.sheetsapp.expression.token.Token;
import com.ibrahimyousre.sheetsapp.functions.CellRange;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;
import com.ibrahimyousre.sheetsapp.functions.SheetFunctions;

class InternalExpressionParser implements ExpressionParser {

    private final SheetsTokenizer tokenizer = new SheetsTokenizer();
    private final FunctionResolver functionResolver;

    private List<Token<TokenType>> tokens;
    private int current;

    public InternalExpressionParser(FunctionResolver functionResolver) {this.functionResolver = functionResolver;}

    @Override
    public SheetFunction parse(String expression) {
        if (!expression.startsWith("=")) {
            return constant(expression);
        } else {
            return parseEquation(expression.substring(1));
        }
    }

    private SheetFunction parseEquation(String equation) {
        tokens = tokenizer.getTokens(equation);
        current = 0;
        return comparisonOrPlusMinusExpression();
    }

    // (plusMinusExpression ==|!=|>|>=|<|<= plusMinusExpression) | plusMinusExpression
    private SheetFunction comparisonOrPlusMinusExpression() {
        SheetFunction a = plusMinusExpression();
        if (canConsume(EQ, NE, GT, GE, LT, LE)) {
            Token<TokenType> operator = consume();
            SheetFunction b = plusMinusExpression();
            switch (operator.getType()) {
                case EQ:
                    return eq(a, b);
                case NE:
                    return ne(a, b);
                case GT:
                    return gt(a, b);
                case GE:
                    return ge(a, b);
                case LT:
                    return lt(a, b);
                case LE:
                    return le(a, b);
            }
        }
        return a;
    }

    // (multiplyDivideModExpression [+-])* multiplyDivideModExpression
    private SheetFunction plusMinusExpression() {
        SheetFunction first = multiplyDivideModExpression();
        while (canConsume(PLUS, MINUS)) {
            Token<TokenType> operator = consume();
            SheetFunction second = multiplyDivideModExpression();
            switch (operator.getType()) {
                case PLUS:
                    first = plus(first, second);
                    break;
                case MINUS:
                    first = minus(first, second);
                    break;
            }
        }
        return first;
    }

    // (powerExpression [*/%])* powerExpression
    private SheetFunction multiplyDivideModExpression() {
        SheetFunction first = powerExpression();
        while (canConsume(MULTIPLY, DIVIDE, MOD)) {
            Token<TokenType> operator = consume();
            SheetFunction second = powerExpression();
            switch (operator.getType()) {
                case MULTIPLY:
                    first = multiply(first, second);
                    break;
                case DIVIDE:
                    first = divide(first, second);
                    break;
                case MOD:
                    first = mod(first, second);
                    break;
            }
        }
        return first;
    }

    // possiblyNegativeExpression (^ powerExpression)
    private SheetFunction powerExpression() {
        SheetFunction first = possiblyNegativeExpression();
        if (canConsume(POWER)) {
            consume();
            SheetFunction second = powerExpression();
            return power(first, second);
        }
        return first;
    }

    // valueExpression | - valueExpression
    private SheetFunction possiblyNegativeExpression() {
        if (canConsume(MINUS)) {
            consume();
            return negate(valueExpression());
        } else {
            return valueExpression();
        }
    }

    // literalExpression | \(comparisonExpression\) | functionIdentifierLiteral\( [parameter[, parameter]*] \)
    private SheetFunction valueExpression() {
        if (canConsume(LP)) {
            consume();
            SheetFunction equation = comparisonOrPlusMinusExpression();
            consume(RP);
            return equation;
        } else if (canConsume(FUNCTION_IDENTIFIER_LITERAL)) {
            Token<TokenType> identifier = consume();
            List<Object> parameters = new LinkedList<>();
            consume(LP);
            if (canConsume(RP)) {
                consume();
            } else {
                parameters.add(parameter());
                while (!canConsume(RP)) {
                    consume(COMMA);
                    parameters.add(parameter());
                }
                consume();
            }
            return functionResolver.resolveFunction(identifier.getData(), parameters.toArray());
        }
        return literalExpression();
    }

    private Object parameter() {
        if (canConsume(CELL_REFERENCE_LITERAL)) {
            Token<TokenType> firstReference = consume();
            if (canConsume(RANGE)) {
                consume();
                Token<TokenType> secondReference = consume(CELL_REFERENCE_LITERAL);
                return new CellRange(firstReference.getData(), secondReference.getData());
            } else {
                return reference(firstReference.getData());
            }
        } else {
            return comparisonOrPlusMinusExpression();
        }
    }

    // numberLiteral | stringLiteral | cellReferenceLiteral
    SheetFunction literalExpression() {
        if (canConsume(NUMBER_LITERAL)) {
            return numberLiteral();
        } else if (canConsume(STRING_LITERAL)) {
            return stringLiteral();
        } else if (canConsume(CELL_REFERENCE_LITERAL)) {
            return cellReferenceLiteral();
        } else {
            throw failExpectation(NUMBER_LITERAL, STRING_LITERAL, CELL_REFERENCE_LITERAL);
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
        return !isDone() && expected == tokens.get(current).getType();
    }

    boolean canConsume(TokenType... expected) {
        return Stream.of(expected).anyMatch(this::canConsume);
    }

    Token<TokenType> consume() {
        return tokens.get(current++);
    }

    Token<TokenType> consume(TokenType expected) {
        if (canConsume(expected)) {
            return consume();
        }
        throw failExpectation(expected);
    }

    private RuntimeException failExpectation(TokenType expectedTypes) {
        String expected = expectedTypes.name();
        Token<TokenType> actual = isDone() ? null : tokens.get(current);
        return ParsingException.unexpectedTokenException(expected, actual, current);
    }

    private RuntimeException failExpectation(TokenType... expectedTypes) {
        String expected = Stream.of(expectedTypes).map(Enum::name).collect(Collectors.joining(" or "));
        Token<TokenType> actual = isDone() ? null : tokens.get(current);
        return ParsingException.unexpectedTokenException(expected, actual, current);
    }
}
