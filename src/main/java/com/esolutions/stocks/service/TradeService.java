package com.esolutions.stocks.service;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TradeService {
    /**
     * Record single trade.
     * @param stockSymbol stock symbol
     * @param timestamp trade timestamp
     * @param quantity stock quantity
     * @param tradeIndicator trade indicator
     * @param price trade price
     * @return Trade model
     */
    Trade recordTrade(String stockSymbol, Date timestamp, int quantity, TradeIndicator tradeIndicator, Money price);

    /**
     * Collect trades for given stock symbols that occured after given timestamp (inclusive).
     * @param stockSymbols stock symbols
     * @param fromTimestamp starting point for the trade collection
     * @return key: stock symbol, value: list of trades
     */
    Map<String, List<Trade>> collectTrades(List<String> stockSymbols, long fromTimestamp);

    /**
     * Utility shortcut to collect trades for last 15 minutes.
     * @param stockSymbols stock symbols.
     * @return @see TradeService.collectTrades
     */
    Map<String, List<Trade>> collectTradesForLast15Min(List<String> stockSymbols);

    /**
     * Calculate GBCE All Share index based on prices calculated for trades that occurred after 'fromTimestamp'.
     * @param fromTimestamp Starting point for collecting trades (required for price calculation)
     * @return GBCE index value as double
     */
    double calculateGBCEAllShareIndex(long fromTimestamp);
}
