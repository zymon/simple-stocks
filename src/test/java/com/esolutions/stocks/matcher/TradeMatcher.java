package com.esolutions.stocks.matcher;

import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

/**
 * Custom matchers for Trade class.
 */
public class TradeMatcher {

    public static Matcher<Trade> hasStockSymbol(String stockSymbol) {
        return new TypeSafeMatcher<Trade>() {
            @Override
            protected boolean matchesSafely(Trade trade) {
                return stockSymbol.equals(trade.getStockSymbol());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" expected stockSymbol: ").appendValue(stockSymbol);
            }
        };
    }

    public static Matcher<Trade> hasQuantity(int quantity) {
        return new TypeSafeMatcher<Trade>() {
            @Override
            protected boolean matchesSafely(Trade trade) {
                return quantity == trade.getQuantity();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" expected quantity: ").appendValue(quantity);
            }
        };
    }

    public static Matcher<Trade> hasTimestamp(Date timestamp) {
        return new TypeSafeMatcher<Trade>() {
            @Override
            protected boolean matchesSafely(Trade trade) {
                return timestamp.getTime() == trade.getTimestamp().getTime();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" expected timestamp: ").appendValue(timestamp.toString());
            }
        };
    }

    public static Matcher<Trade> hasTradeIndicator(TradeIndicator tradeIndicator) {
        return new TypeSafeMatcher<Trade>() {
            @Override
            protected boolean matchesSafely(Trade trade) {
                return tradeIndicator == trade.getIndicator();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" expected trade indicator: ").appendValue(tradeIndicator.name());
            }
        };
    }

    public static Matcher<Trade> hasPrice(Money price) {
        return new TypeSafeMatcher<Trade>() {
            @Override
            protected boolean matchesSafely(Trade trade) {
                return price.equals(trade.getPrice());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" expected price: ").appendValue(price.toString());
            }
        };
    }

}

