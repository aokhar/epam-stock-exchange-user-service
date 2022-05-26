package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertService {
    Page<Alert> getAll(String userId, Pageable pageable);
    void delete(String userId, String alertId);
    void deleteAll(String userId);
    boolean checkAlerts(String userId);
}
