package com.ibrahimyousre.sheetsapp;

import static com.ibrahimyousre.sheetsapp.utils.CellUtils.*;

import com.ibrahimyousre.sheetsapp.utils.CellUtils;

import lombok.Value;
import lombok.With;

@With
@Value
public class Cell {
    int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Cell fromString(String cellName) {
        if (!isValidCellName(cellName)) {
            throw new IllegalArgumentException(cellName);
        }
        int i = skipAlphabetic(cellName, 0);
        int col = getColumnNumber(cellName.substring(0, i));
        int row = Integer.parseInt(cellName.substring(i));
        return new Cell(row, col);
    }

    public String cellName() {
        return CellUtils.getColumnName(col) + row;
    }
}
