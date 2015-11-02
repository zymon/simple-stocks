package com.esolutions.stocks;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Stock;
import com.esolutions.stocks.model.StockType;
import com.esolutions.stocks.util.StockCalculator;
import org.junit.Before;
import org.junit.Test;

import static com.esolutions.stocks.model.StockType.PREFERRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

/**
 * Created by Szymon on 01.11.2015.
 */
public class DividendYieldCalculationTest {
    private StockCalculator stockCalculator = new StockCalculator();
    private Stock teaStock;

    @Before
    public void setUp() {
        teaStock = new Stock.Builder()
                .withSymbol("TEA")
                .withType(StockType.COMMON)
                .withLastDividend(new Money(0.25))
                .withParValue(new Money(2)).build();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullStock() {
        stockCalculator.calculateDividendYield(null, new Money(1));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullTickerPrice() {
        stockCalculator.calculateDividendYield(teaStock, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnZeroTickerPrice() {
        stockCalculator.calculateDividendYield(teaStock, Money.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnInvalidStockType() {
        // given
        teaStock.setType(null);

        // when
        stockCalculator.calculateDividendYield(teaStock, new Money(1));
    }

    @Test
    public void shouldCalculateDividendYieldForCommonStockType() {
        // when
        Money dividendYield = stockCalculator.calculateDividendYield(teaStock, new Money(0.95));

        // then
        assertThat(dividendYield, comparesEqualTo(new Money(0.26)));
    }

    @Test
    public void shouldCalculateZeroDividendYieldForPreferredStockType() {
        // given
        teaStock.setType(PREFERRED);

        // when
        Money dividendYield = stockCalculator.calculateDividendYield(teaStock, new Money(0.95));

        // then
        assertThat(dividendYield, comparesEqualTo(Money.ZERO));
    }

    @Test
    public void shouldCalculateDividendYieldForPreferredStockType() {
        //given
        teaStock.setType(PREFERRED);
        teaStock.setFixedDividend(0.02);

        // when
        Money dividendYield = stockCalculator.calculateDividendYield(teaStock, new Money(0.95));

        // then
        assertThat(dividendYield, comparesEqualTo(new Money(0.04)));
    }
}
