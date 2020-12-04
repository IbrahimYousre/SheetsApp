package com.ibrahimyousre.sheetsapp.expression;

import com.ibrahimyousre.sheetsapp.expression.token.Tokenizer;

public class SheetsTokenizer extends Tokenizer<TokenType> {

    public SheetsTokenizer() {
        super(TokenType.values());
    }
}
