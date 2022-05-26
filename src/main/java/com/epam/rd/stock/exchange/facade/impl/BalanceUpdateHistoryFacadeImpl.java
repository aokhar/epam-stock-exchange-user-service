package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.BalanceUpdateHistoryViewDto;
import com.epam.rd.stock.exchange.facade.BalanceUpdateHistoryFacade;
import com.epam.rd.stock.exchange.model.BalanceUpdateHistory;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import com.epam.rd.stock.exchange.service.BalanceUpdateHistoryService;
import com.epam.rd.stock.exchange.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BalanceUpdateHistoryFacadeImpl implements BalanceUpdateHistoryFacade {
    private final BalanceUpdateHistoryService balanceUpdateHistoryService;
    private final UserService userService;
    @Override
    public Page<BalanceUpdateHistoryViewDto> get(String userEmail, Integer page, Integer size, BalanceUpdateType balanceUpdateType) {
        User user = userService.findByEmail(userEmail);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BalanceUpdateHistory> history = balanceUpdateHistoryService.get(user.getId(), balanceUpdateType, pageable);
        return history.map(this::toUserView);
    }
    private BalanceUpdateHistoryViewDto toUserView(BalanceUpdateHistory balanceUpdateHistory){
        return BalanceUpdateHistoryViewDto.builder()
                .dateTime(balanceUpdateHistory.getDateTime())
                .type(balanceUpdateHistory.getType())
                .update(balanceUpdateHistory.getUpdate())
                .card(balanceUpdateHistory.getCard())
                .build();
    }
}
