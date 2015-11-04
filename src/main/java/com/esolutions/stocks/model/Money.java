package com.esolutions.stocks.model;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.util.Objects.requireNonNull;

/**
 * Representation of money. Introduced to handle floating number inaccurate representation. <br/>
 * Precision and rounding strategy is based on @see java.math.MathContext.DECIMAL64.
 */
public class Money implements Comparable<Money> {
    /**
     * Calculation context.
     */
    private static MathContext CALCULATION_CONTEXT = MathContext.DECIMAL64;
    /**
     * Number scale. Indicates number of important decimal points.
     */
    private static int SCALE = 2;
    /**
     * Multiplication used for conversion to long to shift right decimal points.
     */
    private static BigDecimal SCALE_MULTIPLICATION = new BigDecimal(Math.pow(10, SCALE));
    /**
     * Representation of 0 (zero) money.
     */
    public static final Money ZERO = new Money(0);
    /**
     * Internal representation.
     */
    private BigDecimal value;

    /**
     * Construct money from double (decimal points normalized to @see Money.SCALE)
     * @param amount
     */
    public Money(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    /**
     * Construct money from its internal representation.
     * @param amount
     */
    public Money(BigDecimal amount) {
        this.value = normalizeScale(amount);
    }

    /**
     * Scale amount to SCALE decimal points and do necessary rounding.
     * @param amount
     * @return
     */
    private BigDecimal normalizeScale(BigDecimal amount) {
        return amount.setScale(SCALE, CALCULATION_CONTEXT.getRoundingMode());
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

    /**
     * Represent money as long value without decimal points. <br/>
     * Decimal digits are shifted right, e.g. 1.23 will be 123l.
     * @return
     */
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

    /**
     * Create money from its exact long representation. <br/>
     * It's reverse operation to Money.toExactLong.
     * e.g. 123l will became 1.23 Money.
     * @param tradePrice
     * @return
     */
    public static Money fromExactLong(long tradePrice) {
        return new Money(BigDecimal.valueOf(tradePrice).divide(SCALE_MULTIPLICATION));
    }
}
