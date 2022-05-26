package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.BalanceUpdateHistory;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BalanceUpdateHistoryService {
    Page<BalanceUpdateHistory> get(String userId, BalanceUpdateType changeBalanceType, Pageable pageable);
    void save(BalanceUpdateHistory balanceUpdateHistory);
}
