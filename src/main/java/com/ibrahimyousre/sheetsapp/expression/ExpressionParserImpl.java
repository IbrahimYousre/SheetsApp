package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.functions.SheetFunction;
import com.ibrahimyousre.sheetsapp.functions.SheetFunctions;

public class ExpressionParserImpl implements ExpressionParser {

    @Override
    public SheetFunction parse(String expression) {
        return SheetFunctions.constant(expression);
    }
}
