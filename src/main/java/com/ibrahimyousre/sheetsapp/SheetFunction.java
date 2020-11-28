package com.ibrahimyousre.sheetsapp;

import java.util.function.Function;

@FunctionalInterface
public interface SheetFunction extends Function<SheetAccessor, String> {
}
