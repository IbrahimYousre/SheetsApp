package com.ibrahimyousre.sheetsapp;

import static com.ibrahimyousre.sheetsapp.utils.CellUtils.getRangeInformation;
import static java.util.Collections.emptySet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ibrahimyousre.sheetsapp.expression.ExpressionParser;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;
import com.ibrahimyousre.sheetsapp.utils.CellUtils;

public class CalculationSheet implements Sheet {

    private final Map<String, String> values = new HashMap<>();
    private final Map<String, SheetFunction> functions = new HashMap<>();
    private final DirectedGraph<String> dependencyGraph = new DirectedGraph<>();
    private final SheetFunction emptyCellFunction = (s) -> "";
    private final ExpressionParser expressionParser;

    public CalculationSheet(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public void set(String cell, String expression) {
        SheetFunction function = expressionParser.parse(expression);
        functions.put(cell, function);
        Set<String> referencedCells = new HashSet<>();
        function.getValue(c -> {
            referencedCells.add(c);
            return values.getOrDefault(c, "");
        });
        dependencyGraph.setLinks(referencedCells, cell);
        updateValues(cell);
    }

    private void updateValues(String cell) {
        dependencyGraph.topologicalSortFrom(cell)
                .forEach(c -> values.put(c, functions.getOrDefault(c, emptyCellFunction).getValue(this)));
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
        CellUtils.RangeInformation rangeInfo = getRangeInformation(startCell, endCell);
        String[][] result = new String[rangeInfo.getHeight()][rangeInfo.getWidth()];
        CellUtils.iterateCellRange(startCell, endCell,
                (cell) -> result[cell.getOffsetRow()][cell.getOffsetCol()] = get(cell.getCellName()));
        return result;
    }
}
