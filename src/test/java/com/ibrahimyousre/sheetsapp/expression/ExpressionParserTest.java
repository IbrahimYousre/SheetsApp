package com.ibrahimyousre.sheetsapp.expression;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ibrahimyousre.sheetsapp.functions.SheetAccessor;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;
import com.ibrahimyousre.sheetsapp.functions.SheetFunctions;

@ExtendWith(MockitoExtension.class)
public class ExpressionParserTest {

    @Mock
    private SheetAccessor sheetAccessor;
    @Mock
    private FunctionResolver functionResolver;
    @InjectMocks
    private ExpressionParserImpl expressionParser;

    @Test
    public void testParseNonEquation() {
        assertThat(expressionParser.parse("Hello World").getValue(null))
                .isEqualTo("Hello World");
        assertThat(expressionParser.parse("5").getValue(null))
                .isEqualTo("5");
        assertThat(expressionParser.parse("5+5").getValue(null))
                .isEqualTo("5+5");
    }

    @Test
    public void testParseNumberLiteral() {
        assertThat(expressionParser.parse("=1").getValue(sheetAccessor))
                .isEqualTo("1");
    }

    @Test
    public void testParseStringLiteral() {
        assertThat(expressionParser.parse("='Hello World'").getValue(sheetAccessor))
                .isEqualTo("Hello World");
    }

    @Test
    public void testParseCellReference() {
        when(sheetAccessor.get("A1")).thenReturn("1");
        assertThat(expressionParser.parse("=A1").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        when(sheetAccessor.get("B1")).thenReturn("2");
        assertThat(expressionParser.parse("=B1").getValueAsDouble(sheetAccessor))
                .isEqualTo(2);

    }

    @Test
    public void testParsePlusMinus() {
        assertThat(expressionParser.parse("=10+5").getValueAsDouble(sheetAccessor))
                .isEqualTo(15);
        assertThat(expressionParser.parse("=10-5").getValueAsDouble(sheetAccessor))
                .isEqualTo(5);
        assertThat(expressionParser.parse("=1+1+1+1").getValueAsDouble(sheetAccessor))
                .isEqualTo(4);
        assertThat(expressionParser.parse("=0-1+2-3").getValueAsDouble(sheetAccessor))
                .isEqualTo(-2);
    }

    @Test
    public void testParseMultiplyDivide() {
        assertThat(expressionParser.parse("=10*5").getValueAsDouble(sheetAccessor))
                .isEqualTo(50);
        assertThat(expressionParser.parse("=10/5").getValueAsDouble(sheetAccessor))
                .isEqualTo(2);
        assertThat(expressionParser.parse("=1*1*1*1").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=10/2*5/2").getValueAsDouble(sheetAccessor))
                .isEqualTo(12.5);
    }

    @Test
    public void testBasicArithmeticOperationsPrecedence() {
        assertThat(expressionParser.parse("=1-2*3+4*9/6").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=1+4*9/6-2*3").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=5+2^2*2").getValueAsDouble(sheetAccessor))
                .isEqualTo(13);
        assertThat(expressionParser.parse("=(5+2)^2*2").getValueAsDouble(sheetAccessor))
                .isEqualTo(98);
        assertThat(expressionParser.parse("=(5+2)^(2*2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(7 * 7 * 7 * 7);
    }

    @Test
    public void testBasicArithmeticOperationsPrecedenceWithCellReferences() {
        when(sheetAccessor.get("A1")).thenReturn("1");
        when(sheetAccessor.get("A2")).thenReturn("2");
        when(sheetAccessor.get("A3")).thenReturn("3");
        when(sheetAccessor.get("A4")).thenReturn("4");
        when(sheetAccessor.get("A6")).thenReturn("6");
        when(sheetAccessor.get("A9")).thenReturn("9");
        assertThat(expressionParser.parse("=A1-A2*A3+A4*A9/A6").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=A1+A4*A9/A6-A2*A3").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
    }

    @Test
    public void testParenthesesWorkCorrectly() {
        assertThat(expressionParser.parse("=(1+2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(3);
        assertThat(expressionParser.parse("=(((1)+(2)))").getValueAsDouble(sheetAccessor))
                .isEqualTo(3);
        assertThat(expressionParser.parse("=(((1+1)*(2+2)))").getValueAsDouble(sheetAccessor))
                .isEqualTo(8);
    }

    @Test
    public void testPower() {
        assertThat(expressionParser.parse("=5^2").getValueAsDouble(sheetAccessor))
                .isEqualTo(25);
        assertThat(expressionParser.parse("=5^2^2").getValueAsDouble(sheetAccessor))
                .isEqualTo(625);
    }

    @Test
    public void testMod() {
        assertThat(expressionParser.parse("=10%3").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=10%3%2").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=(10%3)%2").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=10%(3%2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(0);
    }

    @Test
    public void testUnaryMinus() {
        assertThat(expressionParser.parse("=1+-2").getValueAsDouble(sheetAccessor))
                .isEqualTo(-1);
        assertThat(expressionParser.parse("=-1*2").getValueAsDouble(sheetAccessor))
                .isEqualTo(-2);
        assertThat(expressionParser.parse("=1*-2").getValueAsDouble(sheetAccessor))
                .isEqualTo(-2);
        assertThat(expressionParser.parse("=-1*-2").getValueAsDouble(sheetAccessor))
                .isEqualTo(2);
        assertThat(expressionParser.parse("=-1^2").getValueAsDouble(sheetAccessor))
                .isEqualTo(1);
        assertThat(expressionParser.parse("=2^-1").getValueAsDouble(sheetAccessor))
                .isEqualTo(.5);
    }

    @Test
    public void testEqual() {
        assertThat(expressionParser.parse("=1==2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(expressionParser.parse("=2==2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        testNotEqual();
    }


    @Test
    public void testNotEqual() {
        assertThat(expressionParser.parse("=1!=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(expressionParser.parse("=2!=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
    }

    @Test
    public void testLessThan() {
        assertThat(expressionParser.parse("=1<2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(expressionParser.parse("=2<2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(expressionParser.parse("=3<2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
    }

    @Test
    public void testLessOrEqual() {
        assertThat(expressionParser.parse("=1<=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(expressionParser.parse("=2<=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(expressionParser.parse("=3<=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
    }

    @Test
    public void testGreaterThan() {
        assertThat(expressionParser.parse("=1>2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(expressionParser.parse("=2>2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(expressionParser.parse("=3>2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        testGreaterOrEqual();
    }

    @Test
    public void testGreaterOrEqual() {
        assertThat(expressionParser.parse("=1>=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(false);
        assertThat(expressionParser.parse("=2>=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
        assertThat(expressionParser.parse("=3>=2").getValueAsBoolean(sheetAccessor))
                .isEqualTo(true);
    }

    @Test
    public void testFunctionCall() {
        when(functionResolver.resolveFunction(eq("sum"), any()))
                .then((invocation) -> {
                    SheetFunction a = invocation.getArgument(1);
                    SheetFunction b = invocation.getArgument(2);
                    return SheetFunctions.plus(a, b);
                });
        assertThat(expressionParser.parse("=sum(1,2)").getValueAsDouble(sheetAccessor))
                .isEqualTo(3);
        assertThat(expressionParser.parse("=1+sum(2^2+2*2,5%3+1)").getValueAsDouble(sheetAccessor))
                .isEqualTo(12);
        assertThat(expressionParser.parse("=sum(1,sum(2,sum(3,4)))").getValueAsDouble(sheetAccessor))
                .isEqualTo(10);
    }

    @Test
    public void testFunctionWithCellRange() {
        when(functionResolver.resolveFunction(eq("sum"), any()))
                .then((invocation) -> SheetFunctions.sum(invocation.getArgument(1)));
        for (int i = 1; i <= 10; i++) {
            when(sheetAccessor.get("A" + i)).thenReturn("1");
        }
        assertThat(expressionParser.parse("=sum(A1:A10)").getValueAsDouble(sheetAccessor))
                .isEqualTo(10);
    }
}
