package com.esolutions.stocks.dao;

import com.esolutions.stocks.entity.TradeEntity;

import java.util.Collection;
import java.util.List;

public interface TradeDao {

    /**
     * Persist trade.
     * @param stockSymbol stock symbol
     * @param timestampTime trade timestamp
     * @param quantity quantity of stock
     * @param tradeIndicator trade indicator
     * @param tradePrice trade price as long value <br/>
     *                   e.g. 1.23 is represented as 123
     * @return persisted trade representation
     */
    TradeEntity save(String stockSymbol, long timestampTime, int quantity, String tradeIndicator, long tradePrice);

    /**
     * Collect all trades for given stock symbols recorded after fiven timestamp (inclusive)
     * @param stockSymbols stock symbols
     * @param fromTimestamp starting point
     * @return collection of persisted trades
     */
    Collection<TradeEntity> getTrades(List<String> stockSymbols, long fromTimestamp);
}
