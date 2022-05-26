package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.AlertViewDto;
import org.springframework.data.domain.Page;

public interface AlertFacade {
    Page<AlertViewDto> getAll(String userEmail, int page, int pageSize);
    void delete(String userEmail, String alertId);
    void deleteAll(String userEmail);
}
