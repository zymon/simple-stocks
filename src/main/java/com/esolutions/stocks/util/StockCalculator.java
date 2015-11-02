package com.esolutions.stocks.util;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Stock;
import com.esolutions.stocks.model.StockType;
import com.esolutions.stocks.model.Trade;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Created by Szymon on 01.11.2015.
 */
public class StockCalculator {

    public Money calculateDividendYield(Stock stock, Money tickerPrice) {
        requireNonNull(stock, "Missing stock");
        requireNonNull(tickerPrice, "Invalid tickerPrice");
        if (Money.ZERO.equals(tickerPrice)) {
            throw new IllegalArgumentException("Invalid ticker price");
        }

        Money result;
        StockType stockType = stock.getType();
        if (StockType.COMMON == stockType) {
            result = ofNullable(stock.getLastDividend()).orElse(Money.ZERO).divide(tickerPrice);
        } else if (StockType.PREFERRED == stockType) {
            result = new Money(stock.getFixedDividend()).multiply(stock.getParValue()).divide(tickerPrice);
        } else {
            throw new IllegalArgumentException("Invalid stock type: " + stockType);
        }
        return result;
    }

    public double calculatePERatio(Money tickerPrice, Money dividend) {
        requireNonNull(tickerPrice, "Invalid tickerPrice");
        requireNonNull(dividend, "Invalid dividend");

        if (Money.ZERO.equals(dividend)) {
            throw new IllegalArgumentException("Invalid dividend (0)");
        }
        return tickerPrice.divide(dividend).toDouble();
    }

    public Money calculateStockPrice(Collection<Trade> trades) {
        if (trades == null || trades.isEmpty()) {
            throw new IllegalArgumentException("No trades to calculate price");
        }
        int quantities = 0;
        Money quantityPrices = Money.ZERO;
        for(Trade trade : trades) {
            quantities += trade.getQuantity();
            quantityPrices = quantityPrices.add(trade.getPrice().multiply(trade.getQuantity()));
        }
        if (quantities == 0) {
            throw new IllegalArgumentException("Zero quantities");
        }
        return quantityPrices.divide(quantities);
    }
}
