package com.ibrahimyousre.sheetsapp;

public interface Sheet extends SheetAccessor {

    void set(String cell, SheetFunction function);

    void clear(String cell);

    String[][] render(String startCell, String endCell);

}
