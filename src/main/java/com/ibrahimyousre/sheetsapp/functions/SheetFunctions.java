package com.ibrahimyousre.sheetsapp.functions;

public final class SheetFunctions {
    private SheetFunctions() {}

    public static SheetFunction constant(String value) {
        return (sheet) -> value;
    }

    public static SheetFunction reference(String cell) {
        return (sheet) -> sheet.get(cell);
    }

    public static SheetFunction sum(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) + b.getValueAsDouble(sheet));
    }

}
