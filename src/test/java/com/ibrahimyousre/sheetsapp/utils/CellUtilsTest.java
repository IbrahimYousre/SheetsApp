package com.ibrahimyousre.sheetsapp.utils;

import static com.ibrahimyousre.sheetsapp.utils.CellUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CellUtilsTest {

    @Test
    public void testGetColumnName() {
        assertThat(getColumnName(1)).isEqualTo("A");
        assertThat(getColumnName(26)).isEqualTo("Z");
        assertThat(getColumnName(27)).isEqualTo("AA");
        assertThat(getColumnName(52)).isEqualTo("AZ");
        assertThat(getColumnName(26 * 26 + 26)).isEqualTo("ZZ");
        assertThat(getColumnName(26 * 26 + 26 + 1)).isEqualTo("AAA");
    }

    @Test
    public void testGetColumnNumber() {
        assertThat(getColumnNumber("A")).isEqualTo(1);
        assertThat(getColumnNumber("Z")).isEqualTo(26);
        assertThat(getColumnNumber("AA")).isEqualTo(27);
        assertThat(getColumnNumber("AZ")).isEqualTo(52);
        assertThat(getColumnNumber("ZZ")).isEqualTo(26 * 26 + 26);
        assertThat(getColumnNumber("AAA")).isEqualTo(26 * 26 + 26 + 1);
    }

    @Test
    public void testIsValidCellName() {
        assertThat(isValidCellName(null)).isEqualTo(false);
        assertThat(isValidCellName("")).isEqualTo(false);
        assertThat(isValidCellName("1")).isEqualTo(false);
        assertThat(isValidCellName("A0")).isEqualTo(false);
        assertThat(isValidCellName("A1")).isEqualTo(true);
        assertThat(isValidCellName("a1")).isEqualTo(true);
        assertThat(isValidCellName("A19")).isEqualTo(true);
        assertThat(isValidCellName("AB19")).isEqualTo(true);
    }

    @Test
    public void testIsValidColumnName() {
        assertThat(isValidColumnName(null)).isEqualTo(false);
        assertThat(isValidColumnName("")).isEqualTo(false);
        assertThat(isValidColumnName("A")).isEqualTo(true);
        assertThat(isValidColumnName("a")).isEqualTo(true);
        assertThat(isValidColumnName("1")).isEqualTo(false);
        assertThat(isValidColumnName("AZ")).isEqualTo(true);
    }
}