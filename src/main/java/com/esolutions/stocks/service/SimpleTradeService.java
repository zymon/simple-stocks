package com.esolutions.stocks.service;

import com.esolutions.stocks.dao.StockDao;
import com.esolutions.stocks.dao.TradeDao;
import com.esolutions.stocks.entity.StockEntity;
import com.esolutions.stocks.entity.TradeEntity;
import com.esolutions.stocks.mapper.TradeMapper;
import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;
import com.esolutions.stocks.util.StockCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Created by slubieni on 11/2/15.
 */
public class SimpleTradeService implements TradeService {
    private static final long IN_MILLIS_15_MIN = 15 * 60 * 1000;
    private TradeDao tradeDao;
    private TradeMapper tradeMapper;
    private StockDao stockDao;
    private StockCalculator stockCalculator;


    @Override
    public Trade recordTrade(String stockSymbol, Date timestamp, int quantity, TradeIndicator tradeIndicator, Money price) {
        requireNonNull(stockSymbol, "Stock symbol is required");
        requireNonNull(timestamp, "Timestamp is required");
        requireNonNull(tradeIndicator, "Trade indicator is required");
        requireNonNull(price, "Trade price is required");
        if (quantity <= 0 ) {
            throw new IllegalArgumentException("Trade quantity must be greater than zero");
        }

        TradeEntity tradeEntity = tradeDao.save(stockSymbol, timestamp.getTime(), quantity, tradeIndicator.name(), price.toExactLong());
        return tradeMapper.apply(tradeEntity);
    }

    @Override
    public Map<String, List<Trade>> collectTrades(List<String> stockSymbols, long fromTimestamp) {
        if (CollectionUtils.isEmpty(stockSymbols)) {
            throw new IllegalArgumentException("No stock symbols");
        }

        if (fromTimestamp < 0) {
            throw new IllegalArgumentException("Invalid collection period. Must be greater than zero");
        }

        Collection<TradeEntity> trades = tradeDao.getTrades(stockSymbols, fromTimestamp);
        return trades.stream().map(tradeMapper).collect(Collectors.groupingBy(Trade::getStockSymbol));
    }

    @Override
    public Map<String, List<Trade>> collectTradesForLast15Min(List<String> stockSymbols) {
        return collectTrades(stockSymbols, getCurrentTimestamp() - IN_MILLIS_15_MIN);
    }

    private long getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public double calculateGBCEAllShareIndex(long fromTimestamp) {
        Collection<StockEntity> stockEntities = stockDao.getStocks();
        Map<String, List<Trade>> trades = collectTrades(stockEntities.stream().map(stockEntity -> stockEntity.getSymbol()).collect(Collectors.toList()), fromTimestamp);
        if (MapUtils.isEmpty(trades)) {
            throw new IllegalStateException("No trades recorded from timestamp: " + fromTimestamp);
        }

        List<Double> prices = new ArrayList<>(trades.size());
        trades.forEach((key,value) -> prices.add(stockCalculator.calculateStockPrice(value).toDouble()));

        return stockCalculator.calculateGeometricMean(prices);
    }

    public void setTradeDao(TradeDao tradeDao) {
        this.tradeDao = tradeDao;
    }

    public void setTradeMapper(TradeMapper tradeMapper) {
        this.tradeMapper = tradeMapper;
    }

    public void setStockDao(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    public void setStockCalculator(StockCalculator stockCalculator) {
        this.stockCalculator = stockCalculator;
    }
}
