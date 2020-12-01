package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.functions.SheetFunctions.constant;

import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public class ExpressionParserImpl implements ExpressionParser {

    private final EquationParser equationParser;

    public ExpressionParserImpl(EquationParser equationParser) {
        this.equationParser = equationParser;
    }

    @Override
    public SheetFunction parse(String expression) {
        if (!expression.startsWith("=")) {
            return constant(expression);
        } else {
            return parseEquation(expression.substring(1));
        }
    }

    private SheetFunction parseEquation(String equation) {
        return equationParser.parseEquation(equation);
    }
}
