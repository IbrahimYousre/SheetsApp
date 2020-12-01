package com.ibrahimyousre.sheetsapp;

import com.ibrahimyousre.sheetsapp.functions.SheetAccessor;

public interface Sheet extends SheetAccessor {

    void set(String cell, String expression);

    void clear(String cell);

    String[][] render(String startCell, String endCell);

}
