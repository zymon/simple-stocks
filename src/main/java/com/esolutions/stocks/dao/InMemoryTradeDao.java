package com.esolutions.stocks.dao;

import com.esolutions.stocks.entity.TradeEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by slubieni on 11/2/15.
 */
public class InMemoryTradeDao implements TradeDao {

    private List<TradeEntity> trades = new ArrayList();


    @Override
    public TradeEntity save(String stockSymbol, long timestampTime, int quantity, String tradeIndicator, long tradePrice) {
        TradeEntity tradeEntity = new TradeEntity(stockSymbol, timestampTime, quantity, tradeIndicator, tradePrice);
        trades.add(tradeEntity);

        return tradeEntity;
    }

    @Override
    public Collection<TradeEntity> collectTrades(List<String> stockSymbols, long startingTimestamp) {
        return trades.stream().filter(trade -> (trade.getTimestamp() >= startingTimestamp)).collect(Collectors.toList());
    }


}
