package com.esolutions.stocks.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MoneyTest {

    @Test
    public void shouldRoundDoubleUp() {
        // given
        double doubleValue = 0.1553;

        // when
        final Money money = new Money(doubleValue);

        // then
        assertThat(money.toDouble(), is(0.16));
    }

    @Test
    public void shouldRoundDoubleDown() {
        // given
        double doubleValue = 0.1548;

        // when
        final Money money = new Money(doubleValue);

        // then
        assertThat(money.toDouble(), is(0.15));
    }

    @Test
    public void shouldRoundBigDecimalUp() {
        // given
        BigDecimal bigDecimalValue = new BigDecimal(0.15513);

        // when
        final Money money = new Money(bigDecimalValue);

        // then
        final BigDecimal expectedValue = new BigDecimal(0.16).setScale(2, RoundingMode.HALF_EVEN);
        assertThat(money.toBigDecimal(), is(expectedValue));
    }

    @Test
    public void shouldRoundBigDecimalDown() {
        // given
        BigDecimal bigDecimalValue = new BigDecimal(0.15493);

        // when
        final Money money = new Money(bigDecimalValue);

        // then
        final BigDecimal expectedValue = new BigDecimal(0.15).setScale(2, RoundingMode.HALF_EVEN);
        assertThat(money.toBigDecimal(), is(expectedValue));
    }

    @Test
    public void shouldCompareTwoInstances() {
        // given
        Money money1 = new Money(1.231);
        Money money2 = new Money(new BigDecimal(1.231));

        // when
        final int comparisonResult = money1.compareTo(money2);

        // then
        assertThat(comparisonResult, is(0));
    }

    @Test
    public void shouldMultiplyTwoItems() {
        // given
        Money money1 = new Money(1.24);
        Money money2 = new Money(1.32);

        // when
        final Money multiplication = money1.multiply(money2);

        // then
        assertThat(multiplication, comparesEqualTo(new Money(1.64)));
    }

    @Test
    public void shouldMultiplyByDouble() {
        // given
        Money money = new Money(1.24);

        // when
        final Money multiplication = money.multiply(1.32);

        // then
        assertThat(multiplication, comparesEqualTo(new Money(1.64)));
    }

    @Test
    public void shouldDivideTwoItems() {
        // given
        Money money1 = new Money(1.24);
        Money money2 = new Money(1.32);

        // when
        final Money multiplication = money1.divide(money2);

        // then
        assertThat(multiplication, comparesEqualTo(new Money(0.94)));
    }

    @Test
    public void shouldDivideByDouble() {
        // given
        Money money = new Money(1.24);

        // when
        final Money multiplication = money.divide(1.32);

        // then
        assertThat(multiplication, comparesEqualTo(new Money(0.94)));
    }


    @Test(expected = ArithmeticException.class)
    public void shouldThrowExceptionOnDivisionByZero() {
        new Money(1).divide(Money.ZERO);
    }

    @Test
    public void shouldAddTwoItems() {
        // given
        Money money1 = new Money(1.64);
        Money money2 = new Money(1.37);

        // when
        final Money sum = money1.add(money2);

        // then
        assertThat(sum, comparesEqualTo(new Money(3.01)));
    }

    @Test
    public void shouldSubtractTwoItems() {
        // given
        Money money1 = new Money(1.64);
        Money money2 = new Money(1.37);

        // when
        final Money sum = money1.subtract(money2);

        // then
        assertThat(sum, comparesEqualTo(new Money(0.27)));
    }

    @Test
    public void shouldConvertToDouble() {
        // given
        Money money = new Money(1.23422);

        // when
        double doubleValue = money.toDouble();

        // then
        assertThat(doubleValue, is(1.23));
    }

    @Test
    public void shouldConvertToBigDecimal() {
        // given
        Money money = new Money(1.23422);

        // when
        final BigDecimal bigDecimalValue = money.toBigDecimal();

        // then
        assertThat(bigDecimalValue, is(new BigDecimal(1.23).setScale(2, RoundingMode.HALF_EVEN)));
    }

    @Test
    public void shouldConvertToExactLong() {
        // given
        Money money = new Money(1.456);

        // when
        final long exactLongValue = money.toExactLong();

        // then
        assertThat(exactLongValue, is (146l));
    }

    @Test
    public void shouldCreateFromExactLong() {
        // given
        final long exactLong = 14923;

        // when
        final Money money = Money.fromExactLong(exactLong);

        // then
        assertThat(money, comparesEqualTo(new Money(149.23)));
    }
}
