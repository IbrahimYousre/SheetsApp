package com.ibrahimyousre.sheetsapp.functions;

@FunctionalInterface
public interface SheetFunction {

    String getValue(SheetAccessor sheetAccessor);

    default double getValueAsDouble(SheetAccessor sheetAccessor) {
        return Double.parseDouble(getValue(sheetAccessor));
    }

    default boolean getValueAsBoolean(SheetAccessor sheetAccessor) {
        return Boolean.parseBoolean(getValue(sheetAccessor));
    }

}
