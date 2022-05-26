package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.BalanceUpdateHistoryViewDto;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import org.springframework.data.domain.Page;

public interface BalanceUpdateHistoryFacade {
    Page<BalanceUpdateHistoryViewDto> get(String userEmail, Integer page, Integer size, BalanceUpdateType balanceUpdateType);
}
