package com.esolutions.stocks.mapper;

import java.util.function.Function;

/**
 * Created by slubieni on 11/2/15.
 */
public interface Mapper<T, R> extends Function<T,R> {
}
