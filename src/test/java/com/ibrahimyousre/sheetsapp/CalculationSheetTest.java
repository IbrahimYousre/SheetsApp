package com.ibrahimyousre.sheetsapp;

import static com.ibrahimyousre.sheetsapp.SheetFunctions.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CalculationSheetTest {

    @Test
    public void testSetConstantValue() {
        Sheet sheet = new CalculationSheet();
        sheet.set("A1", constant("1"));
        assertThat(sheet.get("A1"))
                .isEqualTo("1");
    }

    @Test
    public void testRenderCell() {
        Sheet sheet = new CalculationSheet();
        sheet.set("A1", constant("1"));
        String[][] render = sheet.render("A1", "A1");
        assertThat(render).hasDimensions(1, 1)
                .isDeepEqualTo(new String[][]{{"1"}});
    }

    @Test
    public void testRenderRange() {
        Sheet sheet = new CalculationSheet();
        sheet.set("A1", constant("1"));
        String[][] render = sheet.render("A1", "B2");
        assertThat(render).hasDimensions(2, 2)
                .isDeepEqualTo(new String[][]{
                        {"1", ""},
                        {"", ""}
                });
    }

    @Test
    public void testCellReference() throws Exception {
        Sheet sheet = new CalculationSheet();
        sheet.set("A1", constant("1"));
        sheet.set("B1", reference("A1"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).hasDimensions(1, 2)
                .isDeepEqualTo(new String[][]{
                        {"1", "1"}
                });
    }

    @Test
    public void testCellReference_emptyCell() throws Exception {
        Sheet sheet = new CalculationSheet();
        sheet.set("A1", reference("B1"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).hasDimensions(1, 2)
                .isDeepEqualTo(new String[][]{
                        {"", ""}
                });
    }

    @Test
    public void testCellReferenceUpdated_scenario1() throws Exception {
        CalculationSheet sheet = new CalculationSheet();
        DirectedGraph<String> spy = sheet.dependencyGraph = Mockito.spy(sheet.dependencyGraph);
        Mockito.when(spy.topologicalSortFrom("A1")).thenReturn(asList("A1"));
        sheet.set("A1", constant("1"));
        Mockito.when(spy.topologicalSortFrom("B1")).thenReturn(asList("B1"));
        sheet.set("B1", reference("A1"));
        Mockito.when(spy.topologicalSortFrom("A1")).thenReturn(asList("A1", "B1"));
        sheet.set("A1", constant("2"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).hasDimensions(1, 2)
                .isDeepEqualTo(new String[][]{
                        {"2", "2"}
                });
    }

    @Test
    public void testCellReferenceUpdated_scenario2() throws Exception {
        CalculationSheet sheet = new CalculationSheet();
        DirectedGraph<String> spy = sheet.dependencyGraph = Mockito.spy(sheet.dependencyGraph);
        Mockito.when(spy.topologicalSortFrom("A1")).thenReturn(asList("A1"));
        sheet.set("A1", reference("B1"));
        Mockito.when(spy.topologicalSortFrom("B1")).thenReturn(asList("B1", "A1"));
        sheet.set("B1", constant("2"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).hasDimensions(1, 2)
                .isDeepEqualTo(new String[][]{
                        {"2", "2"}
                });
    }

}