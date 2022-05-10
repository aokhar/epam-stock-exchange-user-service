package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockService {

    Stock findById(String id);

    Page<Stock> findStocksBySymbol(String symbol, Pageable pageable);
}
