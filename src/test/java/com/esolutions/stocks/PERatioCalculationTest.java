package com.esolutions.stocks;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.util.StockCalculator;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Szymon on 02.11.2015.
 */
public class PERatioCalculationTest {

    private StockCalculator stockCalculator = new StockCalculator();

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullTickerPrice() {
        stockCalculator.calculatePERatio(null, new Money(1));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullDividend() {
        stockCalculator.calculatePERatio(new Money(1), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnZeroDividend() {
        stockCalculator.calculatePERatio(new Money(1), new Money(0));
    }

    @Test
    public void shouldCalculatePERatio() {
        // given
        Money tickerPrice = new Money(0.83);
        Money dividend = new Money(0.04);

        // when
        double peRatio = stockCalculator.calculatePERatio(tickerPrice, dividend);

        // then
        assertThat(peRatio, is(20.75));
    }
}
