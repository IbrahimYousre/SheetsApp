package com.ibrahimyousre.sheetsapp.utils;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.Value;

public final class CellUtils {
    private CellUtils() {
    }

    public static boolean isValidCellName(String cellName) {
        if (cellName == null) { return false; }
        int i = skipAlphabetic(cellName, 0);
        if (i == 0 || i == cellName.length() || cellName.charAt(i) == '0') { return false; }
        return skipNumeric(cellName, i) == cellName.length();
    }

    public static boolean isValidColumnName(String columnName) {
        if (columnName == null || columnName.length() == 0) { return false; }
        return skipAlphabetic(columnName, 0) == columnName.length();
    }

    public static int skipAlphabetic(String cellName, int i) {
        while (i < cellName.length() && (cellName.charAt(i) | 32) >= 'a' && (cellName.charAt(i) | 32) <= 'z') { i++; }
        return i;
    }

    public static int skipNumeric(String cellName, int i) {
        while (i < cellName.length() && cellName.charAt(i) >= '0' && cellName.charAt(i) <= '9') { i++; }
        return i;
    }

    public static int getColumnNumber(String columnName) {
        if (!isValidColumnName(columnName)) {
            throw new IllegalArgumentException(columnName);
        }
        int col = 0;
        for (char c : columnName.toCharArray()) {
            col = col * 26 + (c | 32) - 'a' + 1;
        }
        return col;
    }

    public static String getColumnName(int col) {
        StringBuilder sb = new StringBuilder();
        while (col-- > 0) {
            sb.append((char) ((col) % 26 + 'A'));
            col /= 26;
        }
        return sb.reverse().toString();
    }

    public static int getRow(String cell) {
        int i = skipAlphabetic(cell, 0);
        return Integer.parseInt(cell.substring(i));
    }

    public static int getCol(String cell) {
        int i = skipAlphabetic(cell, 0);
        return getColumnNumber(cell.substring(0, i));
    }

    public static String getCellName(int row, int col) {
        return getColumnName(col) + row;
    }

    public static RangeInformation getRangeInformation(String startCell, String endCell) {
        int[] rowsRange = {getRow(startCell), getRow(endCell)};
        int[] colsRange = {getCol(startCell), getCol(endCell)};
        Arrays.sort(rowsRange);
        Arrays.sort(colsRange);
        return new RangeInformation(rowsRange[0], rowsRange[1], colsRange[0], colsRange[1]);
    }

    public static void iterateCellRange(String startCell, String endCell, Consumer<CellIteratorInformation> callback) {
        iterateCellRange(startCell, endCell, null, (iterInfo, seed) -> {
            callback.accept(iterInfo);
            return null;
        });
    }

    public static <T> T iterateCellRange(String startCell, String endCell, T seed,
            BiFunction<CellIteratorInformation, T, T> callback) {
        RangeInformation rangeInformation = getRangeInformation(startCell, endCell);
        CellIteratorInformation cell = new CellIteratorInformation();
        for (int row = rangeInformation.startRow; row <= rangeInformation.endRow; row++) {
            for (int col = rangeInformation.startCol; col <= rangeInformation.endCol; col++) {
                cell.row = row;
                cell.col = col;
                cell.offsetRow = row - rangeInformation.startRow;
                cell.offsetCol = col - rangeInformation.startCol;
                seed = callback.apply(cell, seed);
            }
        }
        return seed;
    }

    @Value
    public static class RangeInformation {
        int startRow, endRow, startCol, endCol;

        public int getWidth() {
            return endCol - startCol + 1;
        }

        public int getHeight() {
            return endRow - startRow + 1;
        }
    }

    @Getter
    public static class CellIteratorInformation {
        int row, col, offsetRow, offsetCol;

        public String getCellName() {
            return CellUtils.getCellName(row, col);
        }
    }
}
