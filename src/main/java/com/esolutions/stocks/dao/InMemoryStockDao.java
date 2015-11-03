package com.esolutions.stocks.dao;

import com.esolutions.stocks.entity.StockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by slubieni on 11/3/15.
 */
public class InMemoryStockDao implements StockDao {

    private List<StockEntity> stocks = new ArrayList<>();

    @Override
    public StockEntity save(String stockSymbol) {
        StockEntity stockEntity = new StockEntity(stockSymbol);
        stocks.add(stockEntity);
        return stockEntity;
    }

    @Override
    public Collection<StockEntity> collectAllStocks() {
        return stocks;
    }
}
