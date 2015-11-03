package com.esolutions.stocks.service;

import com.esolutions.stocks.dao.InMemoryStockDao;
import com.esolutions.stocks.dao.InMemoryTradeDao;
import com.esolutions.stocks.dao.StockDao;
import com.esolutions.stocks.dao.TradeDao;
import com.esolutions.stocks.mapper.TradeMapper;
import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;
import com.esolutions.stocks.util.StockCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SimpleTradeServiceTest {
    @Mock
    private TradeDao tradeDao;
    @Mock
    private TradeMapper tradeMapper;
    @Mock
    private StockDao stockDao;
    @Mock
    private StockCalculator stockCalculator;

    @InjectMocks
    private TradeService tradeService = new SimpleTradeService();

    @Before public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = NullPointerException.class)
    public void shouldTrowExceptionOnNullStockSymbol() {
        tradeService.recordTrade(null, new Date(), 10, TradeIndicator.BUY, new Money(1.5));
    }

    @Test(expected = NullPointerException.class)
    public void shouldTrowExceptionOnNullTradeTimestamp() {
        tradeService.recordTrade("POP", null, 10, TradeIndicator.BUY, new Money(1.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldTrowExceptionOnZeroQuantity() {
        tradeService.recordTrade("POP", new Date(), 0, TradeIndicator.BUY, new Money(1.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldTrowExceptionOnNegativeQuantity() {
        tradeService.recordTrade("POP", new Date(), -1, TradeIndicator.BUY, new Money(1.5));
    }

    @Test(expected = NullPointerException.class)
    public void shouldTrowExceptionOnNullTradeIndicator() {
        tradeService.recordTrade("POP", new Date(), 10, null, new Money(1.5));
    }

    @Test(expected = NullPointerException.class)
    public void shouldTrowExceptionOnNullTradePrice() {
        tradeService.recordTrade("POP", new Date(), 10, TradeIndicator.BUY, null);
    }

    @Test
    public void shouldRecordTrade() {
        // given
        String stockSymbol = "POP";
        Date timestamp = new Date();
        int quantity = 125;
        TradeIndicator tradeIndicator = TradeIndicator.BUY;
        Money price = new Money(1.73);

        // when
        Trade trade = tradeService.recordTrade(stockSymbol, timestamp, quantity, tradeIndicator, price);

        // then
        assertThat(trade.getStockSymbol(), is(stockSymbol));
        assertThat(trade.getTimestamp().getTime(), is(timestamp.getTime()));
        assertThat(trade.getIndicator(), is(tradeIndicator));
        assertThat(trade.getQuantity(), is(trade.getQuantity()));
        assertThat(trade.getPrice(), comparesEqualTo(price));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnCollectingNullStocks() {
        tradeService.collectTrades(null, new Date().getTime(), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnCollectingEmptyStocks() {
        tradeService.collectTrades(new ArrayList<>(), new Date().getTime(), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNegativeCollectionInterval() {
        tradeService.collectTrades(new ArrayList<>(Arrays.asList("ABC")), new Date().getTime(), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnTooBigCollectionInterval() {
        tradeService.collectTrades(new ArrayList<>(Arrays.asList("ABC")), new Date().getTime(), 61);
    }

    @Test
    public void shouldCollectTradesForLast15Minutes() {
        // given
        String teaStock = "TEA";
        String popStock = "POP";
        String aleStock = "ALE";

        List<String> stockSymbols = new ArrayList<>(Arrays.asList(teaStock, popStock, aleStock));
        LocalDateTime now = LocalDateTime.now();

        tradeService.recordTrade(teaStock, shiftDate(now, 16), 5, TradeIndicator.SELL, new Money(1.25));
        tradeService.recordTrade(teaStock, shiftDate(now, 0), 15, TradeIndicator.SELL, new Money(1.15));
        tradeService.recordTrade(teaStock, shiftDate(now, 14), 18, TradeIndicator.BUY, new Money(1.12));

        tradeService.recordTrade(popStock, shiftDate(now, 1), 125, TradeIndicator.BUY, new Money(0.76));


        // when
        long currentTimestampInMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Map<String, List<Trade>> trades = tradeService.collectTrades(stockSymbols, currentTimestampInMillis, 15);

        // then
        assertThat(trades.size(), is(2));
        Collection<Trade> teaTrades = trades.get(teaStock);
        assertThat(teaTrades.size(), is(2));

        Collection<Trade> popTrades = trades.get(popStock);
        assertThat(popTrades.size(), is(1));

        Collection<Trade> aleTrades = trades.get(aleStock);
        assertThat(aleTrades, nullValue());
    }


    @Test
    public void shouldCalculateGBCEAllShareIndex() {
        // given


        // when
//        tradeService.calculateGBCEAllShareIndex();

        // then
    }

    private Date shiftDate(LocalDateTime now, int minusMinutes) {
        return Date.from(now.minus(minusMinutes, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant());
    }

}