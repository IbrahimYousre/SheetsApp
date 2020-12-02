package com.ibrahimyousre.sheetsapp.functions;

import com.ibrahimyousre.sheetsapp.utils.CellUtils;

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

    public static SheetFunction mod(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) % b.getValueAsDouble(sheet));
    }

    public static SheetFunction negate(SheetFunction a) {
        return (sheet) -> String.valueOf(-a.getValueAsDouble(sheet));
    }

    public static SheetFunction eq(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) == b.getValueAsDouble(sheet));
    }

    public static SheetFunction ne(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) != b.getValueAsDouble(sheet));
    }

    public static SheetFunction gt(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) > b.getValueAsDouble(sheet));
    }

    public static SheetFunction ge(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) >= b.getValueAsDouble(sheet));
    }

    public static SheetFunction lt(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) < b.getValueAsDouble(sheet));
    }

    public static SheetFunction le(SheetFunction a, SheetFunction b) {
        return (sheet) -> String.valueOf(a.getValueAsDouble(sheet) <= b.getValueAsDouble(sheet));
    }

    public static SheetFunction sum(CellRange range) {
        return (sheet) -> String.valueOf(CellUtils.iterateCellRange(range.start, range.end, 0.0,
                (iterInfo, seed) -> seed + Double.parseDouble(sheet.get(iterInfo.getCellName())))
        );
    }
}
