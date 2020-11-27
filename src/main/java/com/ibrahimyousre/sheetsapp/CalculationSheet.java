package com.ibrahimyousre.sheetsapp;

public class CalculationSheet implements Sheet {

    private final CellCoordinateResolver cellCoordinateResolver;
    String[][] values = new String[2][3];

    public CalculationSheet() {cellCoordinateResolver = new CellCoordinateResolver();}

    @Override
    public void setValue(String cellCoordinate, String value) {
        values[0][0] = value;
    }

    @Override
    public String getValue(String cellCoordinate) {
        return values[0][0];
    }

    @Override
    public String[][] render(String startCoordinate, String endCoordinate) {
        int[] start = cellCoordinateResolver.getRowCol(startCoordinate);
        int[] end = cellCoordinateResolver.getRowCol(endCoordinate);
        String[][] result = new String[end[0] - start[0] + 1][end[1] - start[1] + 1];
        for (int row = 0; row < end[0] - start[0] + 1; row++) {
            for (int col = 0; col < end[1] - start[1] + 1; col++) {
                result[row][col] = values[row + start[0] - 1][col + start[1] - 1];
                if (result[row][col] == null) { result[row][col] = ""; }
            }
        }
        return result;
    }
}
