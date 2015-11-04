package com.esolutions.stocks.service;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Szymon on 02.11.2015.
 */
public interface TradeService {
    Trade recordTrade(String stockSymbol, Date timestamp, int quantity, TradeIndicator tradeIndicator, Money price);
    Map<String, List<Trade>> collectTrades(List<String> stockSymbols, long fromTimestamp);
    Map<String, List<Trade>> collectTradesForLast15Min(List<String> stockSymbols);
    double calculateGBCEAllShareIndex(long fromTimestamp);
}
