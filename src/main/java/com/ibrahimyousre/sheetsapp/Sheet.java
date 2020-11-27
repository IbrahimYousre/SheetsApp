package com.ibrahimyousre.sheetsapp;

public interface Sheet {

    void setValue(String cellCoordinate, String value);

    String getValue(String cellCoordinate);

    String[][] render(String startCoordinate, String endCoordinate);

}
