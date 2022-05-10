package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    public StockViewDto toStockDto(Stock stock) {
        return StockViewDto.builder()
                .id(stock.getId())
                .price(stock.getPrice())
                .symbol(stock.getSymbol())
                .name(stock.getName())
                .trend(stock.getTrend())
                .type(stock.getType())
                .build();
    }

    public Page<StockViewDto> toPageStockDto(Page<Stock> stocks) {
        return stocks.map(this::toStockDto);
    }
}
