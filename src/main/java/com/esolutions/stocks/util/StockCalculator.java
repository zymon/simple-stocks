package com.esolutions.stocks.util;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Stock;
import com.esolutions.stocks.model.StockType;
import com.esolutions.stocks.model.Trade;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Utility methods for stock calculations.
 */
public class StockCalculator {

    /**
     * Calculate dividend yield. <br/>
     * For 'common' stock = last dividend / ticker price. <br/>
     * For 'preferred' stock = (fixed dividend * par value) / ticker price.
     * @param stock stock for which the dividend will be calculated
     * @param tickerPrice ticker price
     * @return dividend money amount
     */
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

    /**
     * Calculate P/E Ratio = ticker price / dividend
     * @param tickerPrice ticker price
     * @param dividend dividend
     * @return ratio value as double
     */
    public double calculatePERatio(Money tickerPrice, Money dividend) {
        requireNonNull(tickerPrice, "Invalid tickerPrice");
        requireNonNull(dividend, "Invalid dividend");

        if (Money.ZERO.equals(dividend)) {
            throw new IllegalArgumentException("Invalid dividend (0)");
        }
        return tickerPrice.divide(dividend).toDouble();
    }

    /**
     * Calculate stock price = [Sum of (trade price * stock quantity)] / Sum of stock quantities.
     * @param trades trades of one stock.
     * @return stock price
     */
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

    /**
     * Calculate geometric mean of given prices. (n-th root of n-elements multiplication).
     * @param prices prices
     * @return geometric mean as double value
     */
    public double calculateGeometricMean(List<Double> prices) {
        if (CollectionUtils.isEmpty(prices)) {
            throw new IllegalArgumentException("No prices to calculate geometric mean");
        }
        double product = prices.stream().reduce((price, accumulator) -> accumulator *= price).get();
        return Math.pow(product, 1.0/prices.size());
    }

}
