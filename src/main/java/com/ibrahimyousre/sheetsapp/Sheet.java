package com.ibrahimyousre.sheetsapp;

import com.ibrahimyousre.sheetsapp.functions.SheetAccessor;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public interface Sheet extends SheetAccessor {

    void set(String cell, SheetFunction function);

    void clear(String cell);

    String[][] render(String startCell, String endCell);

}
