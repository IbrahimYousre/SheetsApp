package com.ibrahimyousre.sheetsapp.utils;

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

    public static String getColumnName(long col) {
        StringBuilder sb = new StringBuilder();
        while (col-- > 0) {
            sb.append((char) ((col) % 26 + 'A'));
            col /= 26;
        }
        return sb.reverse().toString();
    }
}
