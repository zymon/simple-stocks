package com.esolutions.stocks.model;

import java.util.Date;

/**
 * Trade model.
 */
public class Trade {
    /**
     * Stock symbol.
     */
    private String stockSymbol;
    /**
     * Quantity of stock.
     */
    private int quantity;
    /**
     * Trade timestamp.
     */
    private Date timestamp;
    /**
     * Trade indicator (buy, sell).
     */
    private TradeIndicator indicator;
    /**
     * Trade price.
     */
    private Money price;

    public Trade(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public TradeIndicator getIndicator() {
        return indicator;
    }

    public Money getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setIndicator(TradeIndicator indicator) {
        this.indicator = indicator;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    /**
     * Utility Trade builder class.
     */
    public static class Builder implements com.esolutions.stocks.model.Builder<Trade> {
        private String stockSymbol;
        private int quantity;
        private Date timestamp;
        private TradeIndicator indicator;
        private Money price;

        public Builder withStockSymbol(String stockSymbol) {
            this.stockSymbol = stockSymbol;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withIndicator(TradeIndicator indicator) {
            this.indicator = indicator;
            return this;
        }

        public Builder withPrice(Money price) {
            this.price = price;
            return this;
        }

        @Override
        public Trade build() {
            Trade trade = new Trade(stockSymbol);
            trade.setIndicator(indicator);
            trade.setPrice(price);
            trade.setQuantity(quantity);
            trade.setTimestamp(timestamp);

            return trade;
        }
    }
}
