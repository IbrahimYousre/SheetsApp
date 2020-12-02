package com.ibrahimyousre.sheetsapp.functions;

public final class SheetFunctions {
    private SheetFunctions() {}

    public static SheetFunction constant(String value) {
        return (sheet) -> value;
    }

    public static SheetFunction reference(String cell) {
        return (sheet) -> sheet.get(cell);
    }

    public static SheetFunction plus(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) + b.getValueAsDouble(sheet));
    }

    public static SheetFunction minus(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) - b.getValueAsDouble(sheet));
    }

    public static SheetFunction multiply(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) * b.getValueAsDouble(sheet));
    }

    public static SheetFunction divide(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) / b.getValueAsDouble(sheet));
    }

    public static SheetFunction power(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(Math.pow(a.getValueAsDouble(sheet), b.getValueAsDouble(sheet)));
    }

    public static SheetFunction negate(SheetFunction a) {
        return (sheet) -> String.valueOf(-a.getValueAsDouble(sheet));
    }

}
