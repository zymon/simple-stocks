package com.esolutions.stocks.mapper;

import java.util.function.Function;

/**
 * Generic mapper interface.
 * @param <T> source class
 * @param <R> target class
 */
public interface Mapper<T, R> extends Function<T,R> {
}
