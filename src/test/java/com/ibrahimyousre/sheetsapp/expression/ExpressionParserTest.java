package com.ibrahimyousre.sheetsapp.expression;

import static com.ibrahimyousre.sheetsapp.functions.SheetFunctions.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;

@ExtendWith(MockitoExtension.class)
public class ExpressionParserTest {

    @Mock
    private EquationParser equationParser;
    @InjectMocks
    private ExpressionParserImpl expressionParser;
    private SheetFunction function;

    @Test
    public void testParseNonEquation() throws Exception {
        function = expressionParser.parse("Hello World");
        assertThat(function.getValue(null)).isEqualTo("Hello World");
        function = expressionParser.parse("5");
        assertThat(function.getValue(null)).isEqualTo("5");
        function = expressionParser.parse("5+5");
        assertThat(function.getValue(null)).isEqualTo("5+5");
        verifyNoInteractions(equationParser);
    }

    @Test
    public void testParseEqualConstant() throws Exception {
        when(equationParser.parseEquation("'Hello World'")).thenReturn(constant("Hello World"));
        function = expressionParser.parse("='Hello World'");
        assertThat(function.getValue(null)).isEqualTo("Hello World");
        verify(equationParser).parseEquation("'Hello World'");

        when(equationParser.parseEquation("5")).thenReturn(constant("5"));
        function = expressionParser.parse("=5");
        assertThat(function.getValue(null)).isEqualTo("5");
        verify(equationParser).parseEquation("5");
    }
}
