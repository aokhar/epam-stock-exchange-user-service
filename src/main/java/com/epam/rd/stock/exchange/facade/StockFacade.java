package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import org.springframework.data.domain.Page;

public interface StockFacade {

    UserStockInfo buy(Order order);

    UserStockInfo sell(Order order);

    Page<ValuableViewDto> findStocksBySymbol(String symbol, int page, int size);

    ValuableViewDto findById(String stockId);
}
