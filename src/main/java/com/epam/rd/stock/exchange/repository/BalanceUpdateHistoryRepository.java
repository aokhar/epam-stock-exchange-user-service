package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.BalanceUpdateHistory;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceUpdateHistoryRepository extends JpaRepository<BalanceUpdateHistory, String> {
    Page<BalanceUpdateHistory> getBalanceUpdateHistoryByUserIdAndTypeOrderByDateTimeDesc(String userId, BalanceUpdateType balanceUpdateType, Pageable pageable);
    Page<BalanceUpdateHistory> getBalanceUpdateHistoryByUserIdOrderByDateTimeDesc(String userId, Pageable pageable);

}
