package com.esolutions.stocks.mapper;

import com.esolutions.stocks.entity.TradeEntity;
import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;

import java.util.Date;

/**
 * TradeEntity to Trade mapper.
 */
public class TradeMapper implements Mapper<TradeEntity, Trade> {

    @Override
    public Trade apply(TradeEntity tradeEntity) {
        return new Trade.Builder()
                .withStockSymbol(tradeEntity.getStockSymbol())
                .withQuantity(tradeEntity.getQuantity())
                .withTimestamp(new Date(tradeEntity.getTimestamp()))
                .withIndicator(TradeIndicator.valueOf(tradeEntity.getTradeIndicator()))
                .withPrice(Money.fromExactLong(tradeEntity.getTradePrice()))
                .build();
    }
}
