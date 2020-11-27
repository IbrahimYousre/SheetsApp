package com.ibrahimyousre.sheetsapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CellCoordinateResolverTest {
    @Test
    public void testCaseA1() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("A1")).containsExactly(1, 1);
    }

    @Test
    public void testCaseB1() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("B1")).containsExactly(2, 1);
    }

    @Test
    public void testCaseB2() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("B2")).containsExactly(2, 2);
    }

    @Test
    public void testCaseAA1() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("AA1")).containsExactly(27, 1);
    }

    @Test
    public void testCaseAB1() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("AB1")).containsExactly(28, 1);
    }

    @Test
    public void testCaseBA1() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("BA1")).containsExactly(53, 1);
    }

    @Test
    public void testCaseBB1() throws Exception {
        CellCoordinateResolver coordinateResolver = new CellCoordinateResolver();
        assertThat(coordinateResolver.getRowCol("BB1")).containsExactly(54, 1);
    }
}
