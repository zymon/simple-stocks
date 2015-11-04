package com.esolutions.stocks.model;

/**
 * Stock model.
 */
public class Stock {
    /**
     * Stock symbol.
     */
    private String symbol;
    /**
     * Type of stock (common, preferred).
     */
    private StockType type;
    /**
     * Amount of last dividend.
     */
    private Money lastDividend;
    /**
     * Value of fixed dividend.
     */
    private double fixedDividend;
    /**
     * Amount of par value.
     */
    private Money parValue;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public StockType getType() {
        return type;
    }

    public void setType(StockType type) {
        this.type = type;
    }

    public Money getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(Money lastDividend) {
        this.lastDividend = lastDividend;
    }

    public double getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public Money getParValue() {
        return parValue;
    }

    public void setParValue(Money parValue) {
        this.parValue = parValue;
    }

    /**
     * Utility Stock builder class.
     */
    public static class Builder implements com.esolutions.stocks.model.Builder<Stock>{
        private String symbol;
        private StockType stockType;
        private Money lastDividend;
        private double fixedDividend;
        private Money parValue;

        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder withType(StockType type) {
            this.stockType = type;
            return this;
        }

        public Builder withLastDividend(Money lastDividend) {
            this.lastDividend = lastDividend;
            return this;
        }

        public Builder withFixedDividend(double fixedDividend) {
            this.fixedDividend = fixedDividend;
            return this;
        }

        public Builder withParValue(Money parValue) {
            this.parValue = parValue;
            return this;
        }

        public Stock build() {
            Stock stock = new Stock();
            stock.setSymbol(symbol);
            stock.setType(stockType);
            stock.setLastDividend(lastDividend);
            stock.setFixedDividend(fixedDividend);
            stock.setParValue(parValue);

            return stock;
        }
    }

}
