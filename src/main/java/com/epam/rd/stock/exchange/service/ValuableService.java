package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Valuable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ValuableService {

    Valuable findById(String id);

    Page<Valuable> findStocksBySymbol(String symbol, Pageable pageable);
}
