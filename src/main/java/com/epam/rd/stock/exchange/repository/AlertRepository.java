package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, String> {
    void deleteAllByUserId(String userId);
    Page<Alert> findAlertByUserIdOrderByDateTimeDesc(String userId, Pageable pageable);
    void deleteAlertById(String id);
    boolean existsAlertsByUserId(String userId);
}
