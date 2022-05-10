package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.StockNotFoundException;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public Page<Stock> findStocksBySymbol(String symbol, Pageable pageable) {
        return stockRepository.findStocksBySymbol(symbol, pageable);
    }

    @Override
    public Stock findById(String id) {
        return stockRepository.findById(id).orElseThrow(() ->
                new StockNotFoundException("Stock with id " + id + " does not exist.")
        );
    }
}
