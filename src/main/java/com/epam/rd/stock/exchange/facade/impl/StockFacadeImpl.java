package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.exception.ProcessOrderException;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.mapper.ValuableMapper;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.service.StockService;
import com.epam.rd.stock.exchange.service.UserStockInfoService;
import com.epam.rd.stock.exchange.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StockFacadeImpl implements StockFacade {

    private final StockService stockService;
    private final UserStockInfoService userStockInfoService;
    private final WalletService walletService;
    private final ValuableMapper valuableMapper;

    @Override
    public Page<ValuableViewDto> findStocksBySymbol(String symbol, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return valuableMapper.toPageStockDto(stockService.findStocksBySymbol(symbol, pageable));
    }

    @Override
    @Transactional
    public UserStockInfo buy(Order order) {
        User user = order.getUser();
        Valuable valuable = order.getValuable();
        UserStockInfo userStockInfo = userStockInfoService.findUserStockInfo
                (user.getId(), valuable.getId());

        boolean enoughMoney = user.getBalance().doubleValue() >= order.getActualOrderPrice().doubleValue();
        if (enoughMoney) {
            if (userStockInfo != null) {
                Integer newAmount = userStockInfo.getAmount() + order.getAmount();
                userStockInfo.setAmount(newAmount);
            } else {
                userStockInfo = UserStockInfo.builder()
                        .stock(valuable)
                        .user(user)
                        .amount(order.getAmount())
                        .build();
            }
        } else {
            throw new ProcessOrderException("User doesn't have enough money for this order.");
        }
        userStockInfoService.save(userStockInfo);
        walletService.changeBalance(user.getId(), order.getOrderPrice().multiply(BigDecimal.valueOf(-1)));
        return userStockInfo;
    }

    @Override
    @Transactional
    public UserStockInfo sell(Order order) {
        User user = order.getUser();
        UserStockInfo userStockInfo = userStockInfoService.findUserStockInfo
                (user.getId(), order.getValuable().getId());
        if (userHasEnoughStocks(userStockInfo, order)) {
            int newAmount = userStockInfo.getAmount() - order.getAmount();
            if (newAmount == 0) {
                userStockInfoService.delete(userStockInfo);
            } else {
                userStockInfo.setAmount(newAmount);
                userStockInfoService.save(userStockInfo);
            }
        } else {
            throw new ProcessOrderException("User doesn't have enough stocks for this order.");
        }
        walletService.changeBalance(user.getId(), order.getOrderPrice());
        return userStockInfo;
    }

    @Override
    public ValuableViewDto findById(String stockId) {
        return valuableMapper.toStockDto(stockService.findById(stockId));
    }

    private boolean userHasEnoughStocks(UserStockInfo userStockInfo, Order order) {
        return userStockInfo != null && (userStockInfo.getAmount() >= order.getAmount());
    }
}

