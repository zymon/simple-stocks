package com.esolutions.stocks;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

/**
 * Created by Szymon on 02.11.2015.
 */
public class StockPriceCalculationTest {

    private StockCalculator stockCalculator = new StockCalculator();

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullTrades() {
        stockCalculator.calculateStockPrice(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoTrades() {
        stockCalculator.calculateStockPrice(new ArrayList<Trade>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTradesWithNoQuantities() {
        // given
        Collection<Trade> trades = new ArrayList<Trade>();
        trades.add(new Trade.Builder().withPrice(new Money(1)).withQuantity(0).build());

        // when
        stockCalculator.calculateStockPrice(trades);
    }


    @Test
    public void shouldCalculateStockPrice() {
        // given
        Collection<Trade> trades = new ArrayList<Trade>();
        trades.add(new Trade.Builder().withPrice(new Money(1.25)).withQuantity(120).build());
        trades.add(new Trade.Builder().withPrice(new Money(0.45)).withQuantity(235).build());
        trades.add(new Trade.Builder().withPrice(new Money(0.16)).withQuantity(94).build());

        // when
        Money stockPrice = stockCalculator.calculateStockPrice(trades);

        // then
        assertThat(stockPrice, comparesEqualTo(new Money(0.60)));
    }
}
