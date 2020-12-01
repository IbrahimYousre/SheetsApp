package com.ibrahimyousre.sheetsapp.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EquationParserTest {

    private final EquationParser equationParser = new EquationParser();

    @Test
    public void testParseNumberLiteral() throws Exception {
        assertThat(equationParser.parseEquation("1").getValue(null))
                .isEqualTo("1");
    }

}