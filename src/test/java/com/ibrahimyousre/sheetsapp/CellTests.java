package com.ibrahimyousre.sheetsapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CellTests {
    @Test
    public void testFromStringForA1() throws Exception {
        assertThat(Cell.fromString("A1")).isEqualTo(new Cell(1, 1));
    }

    @Test
    public void testFromStringForB1() throws Exception {
        assertThat(Cell.fromString("B1")).isEqualTo(new Cell(1, 2));
    }

    @Test
    public void testFromStringForB2() throws Exception {
        assertThat(Cell.fromString("B2")).isEqualTo(new Cell(2, 2));
    }

    @Test
    public void testFromStringForAA1() throws Exception {
        assertThat(Cell.fromString("AA1")).isEqualTo(new Cell(1, 27));
    }

    @Test
    public void testFromStringForAB1() throws Exception {
        assertThat(Cell.fromString("AB1")).isEqualTo(new Cell(1, 28));
    }

    @Test
    public void testFromStringForBA1() throws Exception {
        assertThat(Cell.fromString("BA1")).isEqualTo(new Cell(1, 53));
    }

    @Test
    public void testFromStringForBB1() throws Exception {
        assertThat(Cell.fromString("BB1")).isEqualTo(new Cell(1, 54));
    }
}
