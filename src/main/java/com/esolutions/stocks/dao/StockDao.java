package com.esolutions.stocks.dao;

import com.esolutions.stocks.entity.StockEntity;

import java.util.Collection;

public interface StockDao {

    /**
     * Get all stocks.
     * @return stocks
     */
    Collection<StockEntity> getStocks();

}
