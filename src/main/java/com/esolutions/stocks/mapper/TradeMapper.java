package com.esolutions.stocks.mapper;

import com.esolutions.stocks.entity.TradeEntity;
import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;

import java.util.Date;

/**
 * Created by slubieni on 11/2/15.
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
