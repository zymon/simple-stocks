package com.esolutions.stocks.model;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.util.Objects.requireNonNull;

/**
 * Created by Szymon on 01.11.2015.
 */
public class Money implements Comparable<Money> {
    private static MathContext CALCULATION_CONTEXT = MathContext.DECIMAL64;
    private static int SCALE_INT = 2;
    private static BigDecimal SCALE_MULTIPLICATION = new BigDecimal(Math.pow(10, SCALE_INT));
    public static Money ZERO = new Money(0);

    private BigDecimal value;

    public Money(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    public Money(BigDecimal amount) {
        this.value = normalizeScale(amount);
    }

    private BigDecimal normalizeScale(BigDecimal amount) {
        return amount.setScale(SCALE_INT, CALCULATION_CONTEXT.getRoundingMode());
    }

    @Override
    public int compareTo(Money other) {
        requireNonNull(other);
        return value.compareTo(other.value);
    }

    public Money multiply(Money other) {
        requireNonNull(other);
        return new Money(value.multiply(other.value, CALCULATION_CONTEXT));
    }

    public Money multiply(double value) {
        return multiply(new Money(value));
    }

    public Money divide(double value) {
        return divide(new Money(value));
    }

    public Money divide(Money other) {
       requireNonNull(other);
       return new Money(value.divide(other.value, CALCULATION_CONTEXT));
    }

    public Money add(Money other) {
        requireNonNull(other);
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        requireNonNull(other);
        return new Money(this.value.subtract(other.value));
    }

    public double toDouble() {
        return this.value.doubleValue();
    }

    public BigDecimal toBigDecimal() {
        return this.value;
    }

    public long toExactLong() {
        BigDecimal multiply = this.value.multiply(SCALE_MULTIPLICATION, CALCULATION_CONTEXT);
        return multiply.setScale(0, CALCULATION_CONTEXT.getRoundingMode()).longValueExact();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (value != null ? !value.equals(money.value) : money.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }

    public static Money fromExactLong(long tradePrice) {
        return new Money(BigDecimal.valueOf(tradePrice).divide(SCALE_MULTIPLICATION));
    }
}
