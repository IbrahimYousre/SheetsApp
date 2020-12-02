package com.ibrahimyousre.sheetsapp.expression;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ibrahimyousre.sheetsapp.functions.SheetAccessor;

@ExtendWith(MockitoExtension.class)
class EquationParserTest {

    @Mock
    private SheetAccessor sheetAccessor;

    private final EquationParser equationParser = new EquationParser();

    @Test
    public void testParseNumberLiteral() throws Exception {
        assertThat(equationParser.parseEquation("1").getValue(sheetAccessor))
                .isEqualTo("1");
    }

    @Test
    public void testParseStringLiteral() throws Exception {
        assertThat(equationParser.parseEquation("'Hello World'").getValue(sheetAccessor))
                .isEqualTo("Hello World");
    }

    @Test
    public void testParseCellReference() throws Exception {
        when(sheetAccessor.get("A1")).thenReturn("1");
        assertThat(equationParser.parseEquation("A1").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        when(sheetAccessor.get("B1")).thenReturn("2");
        assertThat(equationParser.parseEquation("B1").getValueAsDouble(sheetAccessor))
                .isEqualTo(2);

    }

    @Test
    public void testParsePlusMinus() throws Exception {
        assertThat(equationParser.parseEquation("10+5").getValueAsDouble(sheetAccessor))
                .isEqualTo(15);
        assertThat(equationParser.parseEquation("10-5").getValueAsDouble(sheetAccessor))
                .isEqualTo(5);
        assertThat(equationParser.parseEquation("1+1+1+1").getValueAsDouble(sheetAccessor))
                .isEqualTo(4);
        assertThat(equationParser.parseEquation("0-1+2-3").getValueAsDouble(sheetAccessor))
                .isEqualTo(-2);
    }

    @Test
    public void testParseMultiplyDivide() throws Exception {
        assertThat(equationParser.parseEquation("10*5").getValueAsDouble(sheetAccessor))
                .isEqualTo(50);
        assertThat(equationParser.parseEquation("10/5").getValueAsDouble(sheetAccessor))
                .isEqualTo(2);
        assertThat(equationParser.parseEquation("1*1*1*1").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("10/2*5/2").getValueAsDouble(sheetAccessor))
                .isEqualTo(12.5);
    }

    @Test
    public void testBasicArithmeticOperationsPrecedence() throws Exception {
        assertThat(equationParser.parseEquation("1-2*3+4*9/6").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("1+4*9/6-2*3").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("5+2^2*2").getValueAsDouble(sheetAccessor))
                .isEqualTo(13);
        assertThat(equationParser.parseEquation("(5+2)^2*2").getValueAsDouble(sheetAccessor))
                .isEqualTo(98);
        assertThat(equationParser.parseEquation("(5+2)^(2*2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(7 * 7 * 7 * 7);
    }

    @Test
    public void testBasicArithmeticOperationsPrecedenceWithCellReferences() throws Exception {
        when(sheetAccessor.get("A1")).thenReturn("1");
        when(sheetAccessor.get("A2")).thenReturn("2");
        when(sheetAccessor.get("A3")).thenReturn("3");
        when(sheetAccessor.get("A4")).thenReturn("4");
        when(sheetAccessor.get("A6")).thenReturn("6");
        when(sheetAccessor.get("A9")).thenReturn("9");
        assertThat(equationParser.parseEquation("A1-A2*A3+A4*A9/A6").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("A1+A4*A9/A6-A2*A3").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
    }

    @Test
    public void testParenthesesWorkCorrectly() throws Exception {
        assertThat(equationParser.parseEquation("(1+2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(3);
        assertThat(equationParser.parseEquation("(((1)+(2)))").getValueAsDouble(sheetAccessor))
                .isEqualTo(3);
        assertThat(equationParser.parseEquation("(((1+1)*(2+2)))").getValueAsDouble(sheetAccessor))
                .isEqualTo(8);
    }

    @Test
    public void testPower() throws Exception {
        assertThat(equationParser.parseEquation("5^2").getValueAsDouble(sheetAccessor))
                .isEqualTo(25);
        assertThat(equationParser.parseEquation("5^2^2").getValueAsDouble(sheetAccessor))
                .isEqualTo(625);
    }

    @Test
    public void testMod() throws Exception {
        assertThat(equationParser.parseEquation("10%3").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("10%3%2").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("(10%3)%2").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("10%(3%2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(0);
    }

    @Test
    public void testUnaryMinus() throws Exception {
        assertThat(equationParser.parseEquation("1+-2").getValueAsDouble(sheetAccessor))
                .isEqualTo(-1);
        assertThat(equationParser.parseEquation("-1*2").getValueAsDouble(sheetAccessor))
                .isEqualTo(-2);
        assertThat(equationParser.parseEquation("1*-2").getValueAsDouble(sheetAccessor))
                .isEqualTo(-2);
        assertThat(equationParser.parseEquation("-1*-2").getValueAsDouble(sheetAccessor))
                .isEqualTo(2);
        assertThat(equationParser.parseEquation("-1^2").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(equationParser.parseEquation("2^-1").getValueAsDouble(sheetAccessor))
                .isEqualTo(.5);
    }

    @Test
    public void testEqual() throws Exception {
        assertThat(equationParser.parseEquation("1==2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(equationParser.parseEquation("2==2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        testNotEqual();
    }


    @Test
    public void testNotEqual() {
        assertThat(equationParser.parseEquation("1!=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(equationParser.parseEquation("2!=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
    }

    @Test
    public void testLessThan() {
        assertThat(equationParser.parseEquation("1<2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(equationParser.parseEquation("2<2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(equationParser.parseEquation("3<2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
    }

    @Test
    public void testLessOrEqual() {
        assertThat(equationParser.parseEquation("1<=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(equationParser.parseEquation("2<=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(equationParser.parseEquation("3<=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
    }

    @Test
    public void testGreaterThan() {
        assertThat(equationParser.parseEquation("1>2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(equationParser.parseEquation("2>2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(equationParser.parseEquation("3>2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        testGreaterOrEqual();
    }

    @Test
    public void testGreaterOrEqual() {
        assertThat(equationParser.parseEquation("1>=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(equationParser.parseEquation("2>=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(equationParser.parseEquation("3>=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
    }

}