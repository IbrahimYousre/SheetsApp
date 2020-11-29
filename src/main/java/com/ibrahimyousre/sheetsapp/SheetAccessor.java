package com.ibrahimyousre.sheetsapp;

@FunctionalInterface
public interface SheetAccessor {
    String get(String cell);
}
