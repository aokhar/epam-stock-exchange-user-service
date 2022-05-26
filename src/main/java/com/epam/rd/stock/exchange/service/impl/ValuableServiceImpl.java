package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.ValuableNotFoundException;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.repository.ValuableRepository;
import com.epam.rd.stock.exchange.service.ValuableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValuableServiceImpl implements ValuableService {

    private final ValuableRepository valuableRepository;

    @Override
    public Page<Valuable> findStocksBySymbol(String symbol, Pageable pageable) {
        return valuableRepository.findStocksBySymbol(symbol, pageable);
    }

    @Override
    public Valuable findById(String id) {
        return valuableRepository.findById(id).orElseThrow(() ->
                new ValuableNotFoundException("Stock with id " + id + " does not exist.")
        );
    }
}
