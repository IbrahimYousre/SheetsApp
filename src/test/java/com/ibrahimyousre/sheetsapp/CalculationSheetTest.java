package com.ibrahimyousre.sheetsapp;

import static com.ibrahimyousre.sheetsapp.functions.SheetFunctions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ibrahimyousre.sheetsapp.expression.ExpressionParser;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

@ExtendWith(MockitoExtension.class)
class CalculationSheetTest {

    @Mock
    private ExpressionParser expressionParser;

    @InjectMocks
    private CalculationSheet sheet;

    private String mockExpressionForFunction(SheetFunction f) {
        when(expressionParser.parse("<PLACEHOLDER>")).thenReturn(f);
        return "<PLACEHOLDER>";
    }

    @Test
    public void testSetConstantValue() {
        sheet.set("A1", mockExpressionForFunction(constant("1")));
        assertThat(sheet.get("A1"))
                .isEqualTo("1");
    }

    @Test
    public void testClearConstant() {
        sheet.set("A1", mockExpressionForFunction(constant("1")));
        sheet.clear("A1");
        assertThat(sheet.get("A1"))
                .isEqualTo("");
    }

    @Test
    public void testRenderCell() {
        sheet.set("A1", mockExpressionForFunction(constant("1")));
        String[][] render = sheet.render("A1", "A1");
        assertThat(render).isDeepEqualTo(new String[][]{{"1"}});
    }

    @Test
    public void testRenderRange() {
        sheet.set("A1", mockExpressionForFunction(constant("1")));
        String[][] render = sheet.render("A1", "B2");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"1", ""},
                {"", ""}
        });
    }

    @Test
    public void testCellReference() {
        sheet.set("A1", mockExpressionForFunction(constant("1")));
        sheet.set("B1", mockExpressionForFunction(reference("A1")));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"1", "1"}
        });
    }

    @Test
    public void testCellReference_emptyCell() {
        sheet.set("A1", mockExpressionForFunction(reference("B1")));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"", ""}
        });
    }

    @Test
    public void testCellReferenceUpdated_scenario1() {
        sheet.set("A1", mockExpressionForFunction(constant("1")));
        sheet.set("B1", mockExpressionForFunction(reference("A1")));
        sheet.set("A1", mockExpressionForFunction(constant("2")));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"2", "2"}
        });
    }

    @Test
    public void testCellReferenceUpdated_scenario2() {
        sheet.set("A1", mockExpressionForFunction(reference("B1")));
        sheet.set("B1", mockExpressionForFunction(constant("2")));
        String[][] render = sheet.render("A1", "B1");
        assertThat(render).isDeepEqualTo(new String[][]{
                {"2", "2"}
        });
    }

    @Test
    public void testSumTwoColumns() {
        sheet.set("A1", mockExpressionForFunction(constant("2")));
        sheet.set("B1", mockExpressionForFunction(constant("3")));
        sheet.set("C1", mockExpressionForFunction(sum(reference("A1"), reference("B1"))));
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(5);
    }

    @Test
    public void testSubtractTwoColumns() {
        sheet.set("A1", mockExpressionForFunction(constant("10")));
        sheet.set("B1", mockExpressionForFunction(constant("3")));
        sheet.set("C1", mockExpressionForFunction(subtract(reference("A1"), reference("B1"))));
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(7);
    }

    @Test
    public void testMultiplyTwoColumns() {
        sheet.set("A1", mockExpressionForFunction(constant("10")));
        sheet.set("B1", mockExpressionForFunction(constant("3")));
        sheet.set("C1", mockExpressionForFunction(multiply(reference("A1"), reference("B1"))));
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(30);
    }

    @Test
    public void testDivideTwoColumns() {
        sheet.set("A1", mockExpressionForFunction(constant("10")));
        sheet.set("B1", mockExpressionForFunction(constant("2")));
        sheet.set("C1", mockExpressionForFunction(divide(reference("A1"), reference("B1"))));
        assertThat(Double.parseDouble(sheet.get("C1"))).isEqualTo(5);
    }

}