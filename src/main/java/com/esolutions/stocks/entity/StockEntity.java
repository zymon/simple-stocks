package com.esolutions.stocks.entity;

public class StockEntity {
    /**
     * Stock symbol
     */
    private String symbol;

    public StockEntity(String stockSymbol) {
        this.symbol = stockSymbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
