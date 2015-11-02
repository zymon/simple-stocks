package com.esolutions.stocks.dao;

import com.esolutions.stocks.entity.TradeEntity;

import java.util.Collection;
import java.util.List;

/**
 * Created by slubieni on 11/2/15.
 */
public interface TradeDao {
    TradeEntity save(String stockSymbol, long timestampTime, int quantity, String tradeIndicator, long tradePrice);

    Collection<TradeEntity> collectTrades(List<String> stockSymbols, long startingTimestamp);
}
