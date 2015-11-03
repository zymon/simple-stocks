package com.esolutions.stocks.entity;

/**
 * Created by slubieni on 11/3/15.
 */
public class StockEntity {

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
