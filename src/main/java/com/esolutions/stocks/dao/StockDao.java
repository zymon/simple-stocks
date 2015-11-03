package com.esolutions.stocks.dao;

import com.esolutions.stocks.entity.StockEntity;

import java.util.Collection;

/**
 * Created by slubieni on 11/3/15.
 */
public interface StockDao {

    StockEntity save(String stockSymbol);
    Collection<StockEntity> collectAllStocks();

}
