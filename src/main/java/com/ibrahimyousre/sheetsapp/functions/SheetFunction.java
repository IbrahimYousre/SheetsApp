package com.ibrahimyousre.sheetsapp.functions;

@FunctionalInterface
public interface SheetFunction {

    String getValue(SheetAccessor sheetAccessor);

    default double getValueAsDouble(SheetAccessor sheetAccessor) {
        return Double.parseDouble(getValue(sheetAccessor));
    }

}
