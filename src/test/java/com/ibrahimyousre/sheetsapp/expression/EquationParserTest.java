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
        assertThat(equationParser.parseEquation("1").getValue(null))
                .isEqualTo("1");
    }

    @Test
    public void testParseStringLiteral() throws Exception {
        assertThat(equationParser.parseEquation("'Hello World'").getValue(null))
                .isEqualTo("Hello World");
    }

    @Test
    public void testParseCellReference() throws Exception {
        when(sheetAccessor.get("A1")).thenReturn("1");
        assertThat(equationParser.parseEquation("A1").getValue(sheetAccessor))
                .isEqualTo("1");
        when(sheetAccessor.get("B1")).thenReturn("2");
        assertThat(equationParser.parseEquation("B1").getValue(sheetAccessor))
                .isEqualTo("2");

    }

}