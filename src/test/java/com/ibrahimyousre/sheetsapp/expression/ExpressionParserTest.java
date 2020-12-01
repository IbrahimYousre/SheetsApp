package com.ibrahimyousre.sheetsapp.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

public class ExpressionParserTest {

    private ExpressionParser expressionParser = new ExpressionParserImpl();
    private SheetFunction function;

    @Test
    public void testParseNonEquation() throws Exception {
        function = expressionParser.parse("Hello World");
        assertThat(function.getValue(null)).isEqualTo("Hello World");
        function = expressionParser.parse("5");
        assertThat(function.getValue(null)).isEqualTo("5");
        function = expressionParser.parse("5+5");
        assertThat(function.getValue(null)).isEqualTo("5+5");
    }
}
