package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.AlertViewDto;
import com.epam.rd.stock.exchange.facade.AlertFacade;
import com.epam.rd.stock.exchange.model.Alert;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.service.AlertService;
import com.epam.rd.stock.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertFacadeImpl implements AlertFacade {

    private final AlertService alertService;

    private final UserService userService;

    @Override
    public Page<AlertViewDto> getAll(String userEmail, int page, int pageSize) {
        User user = userService.findByEmail(userEmail);
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return alertService.getAll(user.getId(), pageable).map(this::toUserView);
    }

    @Override
    @Transactional
    public void delete(String userEmail, String alertId) {
        User user = userService.findByEmail(userEmail);
        alertService.delete(user.getId(), alertId);
    }

    @Override
    @Transactional
    public void deleteAll(String userEmail) {
        User user = userService.findByEmail(userEmail);
        alertService.deleteAll(user.getId());
    }

    private AlertViewDto toUserView(Alert alert){
        return AlertViewDto.builder()
                .id(alert.getId())
                .dateTime(alert.getDateTime())
                .message(alert.getMessage())
                .build();
    }

}
