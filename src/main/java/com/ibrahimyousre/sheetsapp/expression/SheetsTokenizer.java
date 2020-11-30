package com.ibrahimyousre.sheetsapp.expression;

import static java.util.Arrays.asList;

import com.ibrahimyousre.sheetsapp.expression.token.DeterministicTokenMatcher;
import com.ibrahimyousre.sheetsapp.expression.token.RegularTokenMatcher;
import com.ibrahimyousre.sheetsapp.expression.token.Tokenizer;

public class SheetsTokenizer extends Tokenizer {
    public SheetsTokenizer() {
        super(
                new DeterministicTokenMatcher(true, asList(TokenType.values())),
                new RegularTokenMatcher(TokenType.STRING_LITERAL),
                new RegularTokenMatcher(TokenType.NUMBER_LITERAL),
                new RegularTokenMatcher(TokenType.IDENTIFIER_LITERAL)
        );
    }
}
