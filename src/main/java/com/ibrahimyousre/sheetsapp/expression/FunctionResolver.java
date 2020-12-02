package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public interface FunctionResolver {
    SheetFunction resolveFunction(String name, Object... params);
}
