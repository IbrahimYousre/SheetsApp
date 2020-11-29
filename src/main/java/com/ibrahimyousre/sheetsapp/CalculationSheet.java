package com.ibrahimyousre.sheetsapp;

import static java.util.Collections.emptySet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CalculationSheet implements Sheet {

    private final Map<String, String> values = new HashMap<>();
    private final Map<String, SheetFunction> functions = new HashMap<>();
    private final DirectedGraph<String> dependencyGraph = new DirectedGraph<>();
    private final SheetFunction emptyCellFunction = (s) -> "";

    @Override
    public void set(String cell, SheetFunction function) {
        functions.put(cell, function);
        Set<String> referencedCells = new HashSet<>();
        function.apply(c -> {
            referencedCells.add(c);
            return values.getOrDefault(c, "");
        });
        dependencyGraph.setLinks(referencedCells, cell);
        updateValues(cell);
    }

    private void updateValues(String cell) {
        dependencyGraph.topologicalSortFrom(cell)
                .forEach(c -> values.put(c, functions.getOrDefault(c, emptyCellFunction).apply(this)));
    }

    @Override
    public String get(String cell) {
        return values.getOrDefault(cell, "");
    }

    @Override
    public void clear(String cell) {
        dependencyGraph.setLinks(emptySet(), cell);
        functions.remove(cell);
        values.remove(cell);
    }

    @Override
    public String[][] render(String startCell, String endCell) {
        Cell start = Cell.fromString(startCell);
        Cell end = Cell.fromString(endCell);
        if (start.getRow() > end.getRow() || start.getCol() > end.getCol()) {
            throw new IllegalArgumentException();
        }
        String[][] result = new String[end.getRow() - start.getRow() + 1][end.getCol() - start.getCol() + 1];
        Cell i = new Cell(start.getRow(), start.getCol());
        for (; i.getRow() <= end.getRow(); i = i.withRow(i.getRow() + 1).withCol(start.getCol())) {
            for (; i.getCol() <= end.getCol(); i = i.withCol(i.getCol() + 1)) {
                result[i.getRow() - start.getRow()][i.getCol() - start.getCol()] = values
                        .getOrDefault(i.cellName(), "");
            }
        }
        return result;
    }
}
