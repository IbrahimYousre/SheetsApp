package com.ibrahimyousre.sheetsapp.expression;

import java.util.List;

import com.ibrahimyousre.sheetsapp.expression.token.Token;
import com.ibrahimyousre.sheetsapp.functions.SheetFunction;
import com.ibrahimyousre.sheetsapp.functions.SheetFunctions;

public class EquationParser {
    SheetsTokenizer tokenizer = new SheetsTokenizer();
    private List<Token<TokenType>> tokens;
    private int current = 0;

    SheetFunction parseEquation(String equation) {
        tokens = tokenizer.getTokens(equation);
        return numberLiteral();
    }

    Token<TokenType> consume(TokenType expected) {
        if (expected == tokens.get(current).getType()) {
            return tokens.get(current++);
        }
        throw new RuntimeException("Expecting token of type " + expected + " instead found " + tokens.get(current));
    }

    SheetFunction numberLiteral() {
        Token<TokenType> token = consume(TokenType.NUMBER_LITERAL);
        return SheetFunctions.constant(token.getData());
    }
}
