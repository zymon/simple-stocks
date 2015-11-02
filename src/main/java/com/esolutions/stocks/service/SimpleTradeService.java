package com.esolutions.stocks.service;

import com.esolutions.stocks.dao.InMemoryTradeDao;
import com.esolutions.stocks.dao.TradeDao;
import com.esolutions.stocks.entity.TradeEntity;
import com.esolutions.stocks.mapper.TradeMapper;
import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Created by slubieni on 11/2/15.
 */
public class SimpleTradeService implements TradeService {
    private static final int MAX_COLLECTION_INTERVAL = 60;

    private static final long MILLISECONDS_IN_MINUTE = 60*1000;

    private TradeDao tradeDao = new InMemoryTradeDao();
    private TradeMapper tradeMapper = new TradeMapper();

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
}
