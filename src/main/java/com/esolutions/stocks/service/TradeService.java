package com.esolutions.stocks.service;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Szymon on 02.11.2015.
 */
public interface TradeService {
    Trade recordTrade(String stockSymbol, Date timestamp, int quantity, TradeIndicator tradeIndicator, Money price);
    Collection<Trade> collectTrades(int collectionPeriodInMinutes );
}
