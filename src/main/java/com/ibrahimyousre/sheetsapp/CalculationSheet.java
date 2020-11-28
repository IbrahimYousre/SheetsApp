package com.ibrahimyousre.sheetsapp;

import java.util.HashMap;
import java.util.Map;

public class CalculationSheet implements Sheet {

    Map<Cell, String> values = new HashMap<>();
    Map<Cell, SheetFunction> functions = new HashMap<>();

    @Override
    public void setValue(String cellName, SheetFunction function) {
        functions.put(Cell.fromString(cellName), function);
        values.put(Cell.fromString(cellName), function.apply(c -> values.getOrDefault(Cell.fromString(c), "")));
    }

    @Override
    public String getValue(String cellName) {
        return values.getOrDefault(Cell.fromString(cellName), "");
    }

    @Override
    public String[][] render(String startCoordinate, String endCoordinate) {
        Cell start = Cell.fromString(startCoordinate);
        Cell end = Cell.fromString(endCoordinate);
        if (start.getRow() > end.getRow() || start.getCol() > end.getCol()) {
            throw new IllegalArgumentException();
        }
        String[][] result = new String[end.getRow() - start.getRow() + 1][end.getCol() - start.getCol() + 1];
        Cell i = new Cell(start.getRow(), start.getCol());
        for (; i.getRow() <= end.getRow(); i = i.withRow(i.getRow() + 1).withCol(start.getCol())) {
            for (; i.getCol() <= end.getCol(); i = i.withCol(i.getCol() + 1)) {
                result[i.getRow() - start.getRow()][i.getCol() - start.getCol()] = values.getOrDefault(i, "");
            }
        }
        return result;
    }
}
