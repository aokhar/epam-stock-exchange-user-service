package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;
import com.epam.rd.stock.exchange.dto.UserStockInfoViewDto;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import org.springframework.stereotype.Component;

@Component
public class UserStockInfoMapper {
    public UserStockInfoViewDto toUserStockInfoViewDto(UserStockInfo userStockInfo) {
        return UserStockInfoViewDto.builder()
                .id(userStockInfo.getId())
                .amount(userStockInfo.getAmount())
                .price(userStockInfo.getStock().getPrice())
                .symbol(userStockInfo.getStock().getSymbol())
                .trend(userStockInfo.getStock().getTrend())
                .amountToSell(userStockInfo.getAmountToSell())
                .stopLoss(userStockInfo.getStopLoss())
                .takeProfit(userStockInfo.getTakeProfit())
                .build();
    }

    public UserStockInfoUpdateDto toUserStockInfoUpdateDto(UserStockInfo userStockInfo) {
        return UserStockInfoUpdateDto.builder()
                .id(userStockInfo.getId())
                .stockId(userStockInfo.getStock().getId())
                .userId(userStockInfo.getUser().getId())
                .stopLoss(userStockInfo.getStopLoss())
                .takeProfit(userStockInfo.getTakeProfit())
                .amountToSell(userStockInfo.getAmountToSell())
                .build();
    }

    public OrderCreateDto toOrderCreateDto(UserStockInfo userStockInfo) {
        return OrderCreateDto.builder()
                .expectedStockPrice(userStockInfo.getStock().getPrice())
                .stockId(userStockInfo.getStock().getId())
                .userEmail(userStockInfo.getUser().getEmail())
                .amount(userStockInfo.getAmountToSell())
                .build();
    }
}
