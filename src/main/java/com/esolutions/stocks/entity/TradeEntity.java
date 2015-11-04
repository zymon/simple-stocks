package com.esolutions.stocks.entity;

public class TradeEntity {

    /**
     * Stock symbol.
     */
    private String stockSymbol;
    /**
     * Trade timestamp.
     */
    private long timestamp;
    /**
     * Quantity of stock.
     */
    private int quantity;
    /**
     * Trade indicator (buy, sell).
     */
    private String tradeIndicator;
    /**
     * Trade price represented as long. <br/>
     * e.g. 1,23 is represented as 123.
     */
    private long tradePrice;

    public TradeEntity(String stockSymbol, long timestamp, int quantity, String tradeIndicator, long tradePrice) {
        this.stockSymbol = stockSymbol;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.tradeIndicator = tradeIndicator;
        this.tradePrice = tradePrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTradeIndicator() {
        return tradeIndicator;
    }

    public long getTradePrice() {
        return tradePrice;
    }
}
