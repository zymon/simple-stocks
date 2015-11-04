package com.esolutions.stocks.mapper;

import com.esolutions.stocks.entity.TradeEntity;
import com.esolutions.stocks.model.Money;
import com.esolutions.stocks.model.Trade;
import com.esolutions.stocks.model.TradeIndicator;
import org.junit.Test;

import java.util.Date;

import static com.esolutions.stocks.matcher.TradeMatcher.*;
import static org.junit.Assert.assertThat;

/**
 * Created by slubieni on 11/4/15.
 */
public class TradeMapperTest {

    private TradeMapper tradeMapper = new TradeMapper();

    @Test
    public void shouldMapTradeEntityToTrade() {
        // given
        String teaStock = "TEA";
        Date teaTradeDate = new Date();
        int teaQuantity = 68;
        long teaTradePriceInLong = 128;

        TradeEntity tradeEntity = new TradeEntity(teaStock, teaTradeDate.getTime(), teaQuantity, TradeIndicator.SELL.name(), teaTradePriceInLong);

        // when
        final Trade trade = tradeMapper.apply(tradeEntity);

        // then
        assertThat(trade, hasStockSymbol(teaStock));
        assertThat(trade, hasTimestamp(teaTradeDate));
        assertThat(trade, hasQuantity(teaQuantity));
        assertThat(trade, hasTradeIndicator(TradeIndicator.SELL));
        assertThat(trade, hasPrice(Money.fromExactLong(teaTradePriceInLong)));
    }

}