package com.esolutions.stocks;

import com.esolutions.stocks.util.StockCalculator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GeometricMeanCalculationTest {

    private StockCalculator stockCalculator = new StockCalculator();

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullPrices() {
        stockCalculator.calculateGeometricMean(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnEmptyPrices() {
        stockCalculator.calculateGeometricMean(new ArrayList<>());
    }

    @Test
    public void shouldCalculateGeometricMean() {
        // when
        double geometricMean = stockCalculator.calculateGeometricMean(Arrays.asList(2d, 4d, 8d, 4d));

        // then
        assertThat(geometricMean, is(4d));
    }
}
