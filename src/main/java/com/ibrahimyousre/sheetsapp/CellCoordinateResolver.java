package com.ibrahimyousre.sheetsapp;

public class CellCoordinateResolver {
    public int[] getRowCol(String cellCoordinate) {
        cellCoordinate = cellCoordinate.toUpperCase();
        int row = 0;
        int col = 0;
        boolean colStarted = false;
        for (char c : cellCoordinate.toCharArray()) {
            if (c >= '0' && c <= '9') { colStarted = true; }
            if (colStarted) {
                col = col * 10 + c - '0';
            } else {
                row = row * 26 + c - 'A' + 1;
            }
        }
        return new int[]{row, col};
    }
}
