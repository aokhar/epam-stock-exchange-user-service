package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.ValuableHistoryViewDto;
import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.exception.ProcessOrderException;
import com.epam.rd.stock.exchange.facade.ValuableFacade;
import com.epam.rd.stock.exchange.mapper.ValuableMapper;
import com.epam.rd.stock.exchange.model.*;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.ValuableHistoryService;
import com.epam.rd.stock.exchange.service.ValuableService;
import com.epam.rd.stock.exchange.service.UserValuableInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ValuableFacadeImpl implements ValuableFacade {

    private final ValuableService valuableService;
    private final UserValuableInfoService userValuableInfoService;
    private final UserService userService;
    private final ValuableHistoryService valuableHistoryService;
    private final ValuableMapper valuableMapper;

    @Override
    public Page<ValuableViewDto> findStocksBySymbol(String symbol, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return valuableMapper.toPageValuableDto(valuableService.findStocksBySymbol(symbol, pageable));
    }

    @Override
    public UserValuableInfo buy(Order order) {
        User user = order.getUser();
        Valuable valuable = order.getValuable();
        UserValuableInfo userValuableInfo = userValuableInfoService.findUserStockInfo
                (user.getId(), valuable.getId());

        boolean enoughMoney = user.getBalance().doubleValue() >= order.getOrderPrice().doubleValue();
        if (enoughMoney) {
            if (userValuableInfo != null) {
                BigDecimal newAmount = userValuableInfo.getAmount().add(order.getAmount());
                userValuableInfo.setAmount(newAmount);
            } else {
                userValuableInfo = UserValuableInfo.builder()
                        .valuable(valuable)
                        .user(user)
                        .sellAmount(BigDecimal.ZERO)
                        .amount(order.getAmount())
                        .build();
            }
        } else {
            throw new ProcessOrderException("User doesn't have enough money for this order.");
        }
        userValuableInfoService.save(userValuableInfo);
        userService.updateBalance(user.getId(), order.getOrderPrice().multiply(BigDecimal.valueOf(-1)));
        return userValuableInfo;
    }

    @Override
    public UserValuableInfo sell(Order order) {
        User user = order.getUser();
        UserValuableInfo userValuablesInfo = userValuableInfoService.findUserStockInfo
                (user.getId(), order.getValuable().getId());
        if (userHasEnoughStocks(userValuablesInfo, order)) {
            BigDecimal newAmount = userValuablesInfo.getAmount().subtract(order.getAmount());
            if (newAmount.compareTo(BigDecimal.ZERO) == 0) {
                userValuableInfoService.delete(userValuablesInfo);
            } else {
                userValuablesInfo.setAmount(newAmount);
                userValuableInfoService.save(userValuablesInfo);
            }
        } else {
            throw new ProcessOrderException("User doesn't have enough valuables for this order.");
        }
        userService.updateBalance(user.getId(), order.getOrderPrice());
        return userValuablesInfo;
    }

    @Override
    public ValuableViewDto findById(String stockId) {
        return valuableMapper.toValuableDto(valuableService.findById(stockId));
    }

    @Override
    public Page<ValuableHistoryViewDto> findHistoryById(String valuableId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ValuableHistory> valuableHistories = valuableHistoryService.findValuableHistoryByValuableId(valuableId, pageable);
        return valuableHistories.map(this::toUserView);
    }

    private boolean userHasEnoughStocks(UserValuableInfo userValuableInfo, Order order) {
        return userValuableInfo != null &&
                (userValuableInfo.getAmount().subtract(userValuableInfo.getSellAmount()).compareTo(order.getAmount()) >= 0);
    }

    private ValuableHistoryViewDto toUserView(ValuableHistory valuableHistory){
        return ValuableHistoryViewDto.builder()
                .dateTime(valuableHistory.getDateTime())
                .newPrice(valuableHistory.getNewPrice())
                .previousPrice(valuableHistory.getPreviousPrice())
                .trend(valuableHistory.getTrend())
                .build();
    }
}

