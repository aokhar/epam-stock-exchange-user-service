package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.ValuableHistoryViewDto;
import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import org.springframework.data.domain.Page;

public interface ValuableFacade {

    UserValuableInfo buy(Order order);

    UserValuableInfo sell(Order order);

    Page<ValuableViewDto> findStocksBySymbol(String symbol, int page, int size);


    ValuableViewDto findById(String stockId);

    Page<ValuableHistoryViewDto> findHistoryById(String valuableId, int page, int size);
}
