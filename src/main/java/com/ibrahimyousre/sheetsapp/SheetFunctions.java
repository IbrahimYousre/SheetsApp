package com.ibrahimyousre.sheetsapp;

public final class SheetFunctions {
    private SheetFunctions() {}

    public static SheetFunction constant(String value) {
        return (sheet) -> value;
    }

    public static SheetFunction reference(String value) {
        return (sheet) -> sheet.get(value);
    }
}
