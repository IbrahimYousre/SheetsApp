package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public class ExpressionParserImpl implements ExpressionParser {

    private final FunctionResolver functionResolver;

    public ExpressionParserImpl(FunctionResolver functionResolver) {
        this.functionResolver = functionResolver;
    }

    @Override
    public SheetFunction parse(String expression) {
        return new InternalExpressionParser(functionResolver).parse(expression);
    }
}
