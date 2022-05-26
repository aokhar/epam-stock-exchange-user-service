package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.ValuableHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValuableHistoryRepository extends JpaRepository<ValuableHistory, String> {
    Page<ValuableHistory> findByValuableIdOrderByDateTimeDesc(String valuableId, Pageable pageable);
}
