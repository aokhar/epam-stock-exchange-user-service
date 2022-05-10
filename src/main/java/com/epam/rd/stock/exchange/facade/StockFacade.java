package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import org.springframework.data.domain.Page;

public interface StockFacade {

    UserStockInfo buy(Order order);

    UserStockInfo sell(Order order);

    Page<StockViewDto> findStocksBySymbol(String symbol, int page, int size);

    StockViewDto findById(String stockId);
}
