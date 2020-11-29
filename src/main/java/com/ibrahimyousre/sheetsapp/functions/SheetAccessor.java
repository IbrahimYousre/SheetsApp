package com.ibrahimyousre.sheetsapp.functions;

@FunctionalInterface
public interface SheetAccessor {
    String get(String cell);
}
