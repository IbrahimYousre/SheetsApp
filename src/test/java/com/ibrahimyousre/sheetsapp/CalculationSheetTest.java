package com.ibrahimyousre.sheetsapp;

import static com.ibrahimyousre.sheetsapp.SheetFunctions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CalculationSheetTest {

    @Test
    public void testSetConstantValue() {
        Sheet sheet = new CalculationSheet();
        sheet.setValue("A1", constant("1"));
        assertThat(sheet.getValue("A1"))
                .isEqualTo("1");
    }

    @Test
    public void testRenderCell() {
        Sheet sheet = new CalculationSheet();
        sheet.setValue("A1", constant("1"));
        String[][] render = sheet.render("A1", "A1");
        assertThat(render).hasDimensions(1, 1)
                .isDeepEqualTo(new String[][]{{"1"}});
    }

    @Test
    public void testRenderRange() {
        Sheet sheet = new CalculationSheet();
        sheet.setValue("A1", constant("1"));
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
        sheet.setValue("A1", constant("1"));
        sheet.setValue("B1", reference("A1"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).hasDimensions(1, 2)
                .isDeepEqualTo(new String[][]{
                        {"1", "1"}
                });
    }

    @Test
    public void testCellReference_emptyCell() throws Exception {
        Sheet sheet = new CalculationSheet();
        sheet.setValue("A1", reference("B1"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).hasDimensions(1, 2)
                .isDeepEqualTo(new String[][]{
                        {"", ""}
                });
    }

}