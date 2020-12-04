package com.ibrahimyousre.sheetsapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ibrahimyousre.sheetsapp.expression.ExpressionParserImpl;

class CalculationSheetIntegrationTest {

    private CalculationSheet sheet;

    @BeforeEach
    public void setup() {
        sheet = new CalculationSheet(new ExpressionParserImpl(null));
    }

    @Test
    public void testSetConstantValue() {
        sheet.set("A1", "=1");
        assertThat(sheet.get("A1"))
                .isEqualTo("1");
    }

    @Test
    public void testClearConstant() {
        sheet.set("A1", "=1");
        sheet.clear("A1");
        assertThat(sheet.get("A1"))
                .isEqualTo("");
    }

    @Test
    public void testRenderCell() {
        sheet.set("A1", "=1");
        String[][] render = sheet.render("A1", "A1");
        assertThat(render).isDeepEqualTo(new String[][]{{"1"}});
    }

    @Test
    public void testRenderRange() {
        sheet.set("A1", "=1");
        String[][] render = sheet.render("A1", "B2");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"1", ""},
                {"", ""}
        });
    }

    @Test
    public void testCellReference() {
        sheet.set("A1", "=1");
        sheet.set("B1", "=A1");
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"1", "1"}
        });
    }

    @Test
    public void testCellReference_emptyCell() {
        sheet.set("A1", "=B1");
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"", ""}
        });
    }

    @Test
    public void testCellReferenceUpdated_scenario1() {
        sheet.set("A1", "=1");
        sheet.set("B1", "=A1");
        sheet.set("A1", "=2");
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"2", "2"}
        });
    }

    @Test
    public void testCellReferenceUpdated_scenario2() {
        sheet.set("A1", "=B1");
        sheet.set("B1", "=2");
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"2", "2"}
        });
    }

    @Test
    public void testSumTwoColumns() {
        sheet.set("A1", "=2");
        sheet.set("B1", "=3");
        sheet.set("C1", "=A1+B1");
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(5);
    }

    @Test
    public void testSubtractTwoColumns() {
        sheet.set("A1", "=10");
        sheet.set("B1", "=3");
        sheet.set("C1", "=A1-B1");
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(7);
    }

    @Test
    public void testMultiplyTwoColumns() {
        sheet.set("A1", "=10");
        sheet.set("B1", "=3");
        sheet.set("C1", "=A1*B1");
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(30);
    }

    @Test
    public void testDivideTwoColumns() {
        sheet.set("A1", "=10");
        sheet.set("B1", "=2");
        sheet.set("C1", "=A1/B1");
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(5);
    }

}