package com.esolutions.stocks.service;

import com.esolutions.stocks.dao.InMemoryStockDao;
import com.esolutions.stocks.dao.InMemoryTradeDao;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Created by slubieni on 11/2/15.
 */
public class SimpleTradeService implements TradeService {
    private static final int MAX_COLLECTION_INTERVAL = 60;

    private static final long MILLISECONDS_IN_MINUTE = 60*1000;

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
    public Map<String, List<Trade>> collectTrades(List<String> stockSymbols, long currentTimestamp, int collectionPeriodInMinutes) {
        if (CollectionUtils.isEmpty(stockSymbols)) {
            throw new IllegalArgumentException("No stock symbols");
        }

        if (collectionPeriodInMinutes < 0 || collectionPeriodInMinutes > MAX_COLLECTION_INTERVAL) {
            throw new IllegalArgumentException("Invalid collection period. Must be in range [0;" + MAX_COLLECTION_INTERVAL + "]");
        }

        Collection<TradeEntity> trades = tradeDao.collectTrades(stockSymbols, currentTimestamp - collectionPeriodInMinutes * MILLISECONDS_IN_MINUTE);

        return trades.stream().map(tradeMapper).collect(Collectors.groupingBy(Trade::getStockSymbol));
    }

    /**
     *
     * @param currentTimestamp
     * @param collectionPeriodInMinutes
     * @return
     * TODO: what is there was no trade for a stock during given collection period?
     */
    @Override
    public double calculateGBCEAllShareIndex(long currentTimestamp, int collectionPeriodInMinutes) {
        Collection<StockEntity> stockEntities = stockDao.collectAllStocks();
        Map<String, List<Trade>> trades = collectTrades(stockEntities.stream().map(stockEntity -> stockEntity.getSymbol()).collect(Collectors.toList()), currentTimestamp, collectionPeriodInMinutes);
        if (MapUtils.isEmpty(trades)) {
            throw new IllegalStateException("No trades recorded in given collection period of " + collectionPeriodInMinutes + " minutes");
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
