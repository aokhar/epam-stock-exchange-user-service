package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.AlertDoesntExistException;
import com.epam.rd.stock.exchange.exception.UserDontHaveAccessToThisAlertException;
import com.epam.rd.stock.exchange.model.Alert;
import com.epam.rd.stock.exchange.repository.AlertRepository;
import com.epam.rd.stock.exchange.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Override
    public Page<Alert> getAll(String userId, Pageable pageable) {
        return alertRepository.findAlertByUserIdOrderByDateTimeDesc(userId, pageable);
    }

    @Override
    public void delete(String userId, String alertId) {
        Optional<Alert> alertOptional = alertRepository.findById(alertId);
        if(alertOptional.isEmpty()){
            throw new AlertDoesntExistException("Alert doesnt exist");
        }
        Alert alert = alertOptional.get();
        if(!alert.getUserId().equals(userId)){
            throw new UserDontHaveAccessToThisAlertException("User dont have access to this alert");
        }
        alertRepository.delete(alert);
    }

    @Override
    public void deleteAll(String userId) {
        alertRepository.deleteAllByUserId(userId);
    }

    @Override
    public boolean checkAlerts(String userId) {
        return alertRepository.existsAlertsByUserId(userId);
    }
}
