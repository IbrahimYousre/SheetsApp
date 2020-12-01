package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public interface ExpressionParser {
    SheetFunction parse(String expression);
}
