package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.exception.ProcessOrderException;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.mapper.StockMapper;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Wallet;
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
    private final StockMapper stockMapper;

    @Override
    public Page<StockViewDto> findStocksBySymbol(String symbol, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return stockMapper.toPageStockDto(stockService.findStocksBySymbol(symbol, pageable));
    }

    @Override
    @Transactional
    public UserStockInfo buy(Order order) {
        User user = order.getUser();
        Stock stock = order.getStock();
        UserStockInfo userStockInfo = userStockInfoService.findUserStockInfo
                (user.getId(), stock.getId());
        Wallet wallet = walletService.findByUserId(user.getId());

        boolean enoughMoney = wallet.getBalance().doubleValue() >= order.getActualOrderPrice().doubleValue();
        if (enoughMoney) {
            if (userStockInfo != null) {
                Integer newAmount = userStockInfo.getAmount() + order.getAmount();
                userStockInfo.setAmount(newAmount);
            } else {
                userStockInfo = UserStockInfo.builder()
                        .stock(stock)
                        .user(user)
                        .amount(order.getAmount())
                        .build();
            }
        } else {
            throw new ProcessOrderException("User doesn't have enough money for this order.");
        }
        userStockInfoService.save(userStockInfo);
        walletService.changeBalance(user.getId(), order.getActualOrderPrice().multiply(BigDecimal.valueOf(-1)));
        return userStockInfo;
    }

    @Override
    @Transactional
    public UserStockInfo sell(Order order) {
        User user = order.getUser();
        UserStockInfo userStockInfo = userStockInfoService.findUserStockInfo
                (user.getId(), order.getStock().getId());
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
        walletService.changeBalance(user.getId(), order.getActualOrderPrice());
        return userStockInfo;
    }

    @Override
    public StockViewDto findById(String stockId) {
        return stockMapper.toStockDto(stockService.findById(stockId));
    }

    private boolean userHasEnoughStocks(UserStockInfo userStockInfo, Order order) {
        return userStockInfo != null && (userStockInfo.getAmount() >= order.getAmount());
    }
}

