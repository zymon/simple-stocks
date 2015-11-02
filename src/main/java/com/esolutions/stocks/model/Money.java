package com.esolutions.stocks.model;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.util.Objects.requireNonNull;

/**
 * Created by Szymon on 01.11.2015.
 */
public class Money implements Comparable<Money> {
    private static MathContext CALCULATION_CONTEXT = MathContext.DECIMAL64;
    public static Money ZERO = new Money(0);

    private BigDecimal value;

    public Money(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    public Money(BigDecimal amount) {
        this.value = normalizeScale(amount);
    }

    private BigDecimal normalizeScale(BigDecimal amount) {
        return amount.setScale(2, CALCULATION_CONTEXT.getRoundingMode());
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
}
