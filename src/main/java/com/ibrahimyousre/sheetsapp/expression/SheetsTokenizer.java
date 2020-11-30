package com.ibrahimyousre.sheetsapp.expression;

import static java.util.Arrays.asList;

import com.ibrahimyousre.sheetsapp.expression.token.CompositeTokenMatcher;
import com.ibrahimyousre.sheetsapp.expression.token.DeterministicTokenMatcher;
import com.ibrahimyousre.sheetsapp.expression.token.RegularTokenMatcher;
import com.ibrahimyousre.sheetsapp.expression.token.Tokenizer;

public class SheetsTokenizer extends Tokenizer {
    public SheetsTokenizer() {
        super(new CompositeTokenMatcher(asList(
                new DeterministicTokenMatcher(true),
                new RegularTokenMatcher(TokenType.STRING_LITERAL),
                new RegularTokenMatcher(TokenType.NUMBER_LITERAL),
                new RegularTokenMatcher(TokenType.IDENTIFIER_LITERAL)
        )));
    }
}
