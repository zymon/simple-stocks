package com.esolutions.stocks.service;

import com.esolutions.stocks.dao.StockDao;
import com.esolutions.stocks.dao.TradeDao;
import com.esolutions.stocks.entity.StockEntity;
import com.esolutions.stocks.entity.TradeEntity;
import com.esolutions.stocks.mapper.TradeMapper;
import com.esolutions.stocks.matcher.TradeMatcher;
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

import static com.esolutions.stocks.matcher.TradeMatcher.*;
import static java.util.Date.from;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleTradeServiceTest {
    @Mock
    private TradeDao tradeDao;
    @Mock
    private StockDao stockDao;

    @InjectMocks
    private SimpleTradeService tradeService = new SimpleTradeService();

    @Before public void setUp() {
        MockitoAnnotations.initMocks(this);
        tradeService.setTradeMapper(new TradeMapper());
        tradeService.setStockCalculator(new StockCalculator());
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

        TradeEntity tradeEntity = new TradeEntity(stockSymbol, timestamp.getTime(), quantity, tradeIndicator.name(), price.toExactLong());
        when(tradeDao.save(eq(stockSymbol), eq(timestamp.getTime()), eq(quantity),
                           eq(tradeIndicator.name()), eq(price.toExactLong()))).thenReturn(tradeEntity);

        // when
        Trade trade = tradeService.recordTrade(stockSymbol, timestamp, quantity, tradeIndicator, price);

        // then
        verify(tradeDao).save(stockSymbol, timestamp.getTime(), quantity, tradeIndicator.name(), price.toExactLong());

        assertThat(trade, hasStockSymbol(stockSymbol));
        assertThat(trade, hasTimestamp(timestamp));
        assertThat(trade, hasTradeIndicator(tradeIndicator));
        assertThat(trade, hasQuantity(quantity));
        assertThat(trade, hasPrice(price));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnCollectingNullStocks() {
        tradeService.collectTrades(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnCollectingEmptyStocks() {
        tradeService.collectTrades(new ArrayList<>(), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNegativeCollectionInterval() {
        tradeService.collectTrades(new ArrayList<>(Arrays.asList("ABC")), -1);
    }

    @Test
    public void shouldCollectTradesForLast15Minutes() {
        // given
        String teaStock = "TEA";
        String popStock = "POP";
        String aleStock = "ALE";

        List<String> stockSymbols = new ArrayList<>(Arrays.asList(teaStock, popStock, aleStock));

        LocalDateTime now = LocalDateTime.now();
        int fromTimestamp = 1521342;

        Date teaTradeDate = shiftDate(now, 0);
        Date popTradeDate = shiftDate(now, 1);

        int teaQuantity = 68;
        int popQuantity = 125;
        long teaTradePriceInLong = 115;
        long popTradePriceInLong = 76;

        Collection<TradeEntity> tradeEntities = new ArrayList<>(Arrays.asList(
                new TradeEntity(teaStock, teaTradeDate.getTime(), teaQuantity, TradeIndicator.SELL.name(), teaTradePriceInLong),
                new TradeEntity(popStock, popTradeDate.getTime(), popQuantity, TradeIndicator.BUY.name(), popTradePriceInLong)
        ));

        when(tradeDao.getTrades(anyList(), anyLong())).thenReturn(tradeEntities);

        // when
        Map<String, List<Trade>> trades = tradeService.collectTrades(stockSymbols, fromTimestamp);

        // then
        verify(tradeDao).getTrades(stockSymbols, fromTimestamp);

        assertThat(trades.size(), is(2));
        Collection<Trade> teaTrades = trades.get(teaStock);
        assertThat(teaTrades.size(), is(1));

        Trade teaTrade = teaTrades.iterator().next();
        assertThat(teaTrade, hasStockSymbol(teaStock));
        assertThat(teaTrade, TradeMatcher.hasQuantity(teaQuantity));
        assertThat(teaTrade, TradeMatcher.hasTimestamp(teaTradeDate));
        assertThat(teaTrade, TradeMatcher.hasTradeIndicator(TradeIndicator.SELL));
        assertThat(teaTrade, TradeMatcher.hasPrice(Money.fromExactLong(teaTradePriceInLong)));


        Collection<Trade> popTrades = trades.get(popStock);
        assertThat(popTrades.size(), is(1));
        Trade popTrade = popTrades.iterator().next();

        assertThat(popTrade, hasStockSymbol(popStock));
        assertThat(popTrade, TradeMatcher.hasQuantity(popQuantity));
        assertThat(popTrade, TradeMatcher.hasTimestamp(popTradeDate));
        assertThat(popTrade, TradeMatcher.hasTradeIndicator(TradeIndicator.BUY));
        assertThat(popTrade, TradeMatcher.hasPrice(Money.fromExactLong(popTradePriceInLong)));


        Collection<Trade> aleTrades = trades.get(aleStock);
        assertThat(aleTrades, nullValue());
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenNoTradesFoundForGBCEAllShareIndex() {
        // given
        Collection<StockEntity> stocks = new ArrayList<>(Arrays.asList(
                new StockEntity("TEA")
        ));

        when(stockDao.getStocks()).thenReturn(stocks);

        // when
        tradeService.calculateGBCEAllShareIndex(123);
    }


    @Test
    public void shouldCalculateGBCEAllShareIndex() {
        // given
        int fromTimestamp = 1232131;

        final String teaSymbol = "TEA";
        final String popSymbol = "POP";
        final String aleSymbol = "ALE";
        Collection<StockEntity> stocks = new ArrayList<>(Arrays.asList(
                new StockEntity(teaSymbol),
                new StockEntity(popSymbol),
                new StockEntity(aleSymbol)
        ));
        Collection<TradeEntity> trades = new ArrayList<>(Arrays.asList(
                new TradeEntity(teaSymbol, 4, 10, TradeIndicator.BUY.name(), 105),
                new TradeEntity(teaSymbol, 2, 87, TradeIndicator.BUY.name(), 95),
                new TradeEntity(popSymbol, 3, 123, TradeIndicator.SELL.name(), 136)
        ));

        when(stockDao.getStocks()).thenReturn(stocks);
        when(tradeDao.getTrades(anyList(), anyLong())).thenReturn(trades);

        // when
        double gbceAllShareIndex = tradeService.calculateGBCEAllShareIndex(fromTimestamp);

        // then
        verify(stockDao).getStocks();
        assertThat(gbceAllShareIndex, closeTo(1.142d, 0.001d));
    }

    private Date shiftDate(LocalDateTime now, int minusMinutes) {
        return from(now.minus(minusMinutes, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant());
    }

}