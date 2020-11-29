package com.ibrahimyousre.sheetsapp;

import static com.ibrahimyousre.sheetsapp.SheetFunctions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculationSheetTest {

    private Sheet sheet;

    @BeforeEach
    public void setup() {
        sheet = new CalculationSheet();
    }

    @Test
    public void testSetConstantValue() {
        sheet.set("A1", constant("1"));
        assertThat(sheet.get("A1"))
                .isEqualTo("1");
    }

    @Test
    public void testClearConstant() {
        sheet.clear("A1");
        assertThat(sheet.get("A1"))
                .isEqualTo("");
    }

    @Test
    public void testRenderCell() {
        sheet.set("A1", constant("1"));
        String[][] render = sheet.render("A1", "A1");
        assertThat(render).isDeepEqualTo(new String[][]{{"1"}});
    }

    @Test
    public void testRenderRange() {
        sheet.set("A1", constant("1"));
        String[][] render = sheet.render("A1", "B2");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"1", ""},
                {"", ""}
        });
    }

    @Test
    public void testCellReference() {
        sheet.set("A1", constant("1"));
        sheet.set("B1", reference("A1"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"1", "1"}
        });
    }

    @Test
    public void testCellReference_emptyCell() {
        sheet.set("A1", reference("B1"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"", ""}
        });
    }

    @Test
    public void testCellReferenceUpdated_scenario1() {
        sheet.set("A1", constant("1"));
        sheet.set("B1", reference("A1"));
        sheet.set("A1", constant("2"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"2", "2"}
        });
    }

    @Test
    public void testCellReferenceUpdated_scenario2() {
        sheet.set("A1", reference("B1"));
        sheet.set("B1", constant("2"));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"2", "2"}
        });
    }

    @Test
    public void testSumTwoColumns() throws Exception {
        sheet.set("A1", constant("1"));
        sheet.set("B1", constant("1"));
        sheet.set("C1", sum(reference("A1"), reference("B1")));
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(2);
        sheet.set("A1", constant("2"));
        sheet.set("B1", constant("3"));
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(5);
    }

}