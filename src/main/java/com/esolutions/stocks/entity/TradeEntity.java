package com.esolutions.stocks.entity;

/**
 * Created by slubieni on 11/2/15.
 */
public class TradeEntity {

    private String stockSymbol;
    private long timestamp;
    private int quantity;
    private String tradeIndicator;
    long tradePrice;

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
