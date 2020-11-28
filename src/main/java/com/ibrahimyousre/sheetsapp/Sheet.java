package com.ibrahimyousre.sheetsapp;

public interface Sheet {

    void setValue(String cellCoordinate, SheetFunction function);

    String getValue(String cellCoordinate);

    String[][] render(String startCoordinate, String endCoordinate);

}
