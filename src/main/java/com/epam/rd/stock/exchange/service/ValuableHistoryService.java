package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.ValuableHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ValuableHistoryService {
    Page<ValuableHistory> findValuableHistoryByValuableId(String valuableId, Pageable pageable);
}
