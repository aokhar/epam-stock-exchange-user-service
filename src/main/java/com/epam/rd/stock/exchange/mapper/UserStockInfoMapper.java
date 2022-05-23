package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;
import com.epam.rd.stock.exchange.dto.UserValuableInfoViewDto;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import org.springframework.stereotype.Component;

@Component
public class UserStockInfoMapper {
    public UserValuableInfoViewDto toUserStockInfoViewDto(UserStockInfo userStockInfo) {
        return UserValuableInfoViewDto.builder()
                .id(userStockInfo.getId())
                .amount(userStockInfo.getAmount())
                .price(userStockInfo.getStock().getPrice())
                .symbol(userStockInfo.getStock().getSymbol())
                .trend(userStockInfo.getStock().getTrend())
                .build();
    }

    public UserStockInfoUpdateDto toUserStockInfoUpdateDto(UserStockInfo userStockInfo) {
        return UserStockInfoUpdateDto.builder()
                .id(userStockInfo.getId())
                .valuableId(userStockInfo.getStock().getId())
                .userId(userStockInfo.getUser().getId())
                .build();
    }

    public OrderCreateDto toOrderCreateDto(UserStockInfo userStockInfo) {
        return OrderCreateDto.builder()
                .valuablePrice(userStockInfo.getStock().getPrice())
                .valuableId(userStockInfo.getStock().getId())
                .userEmail(userStockInfo.getUser().getEmail())
                .build();
    }
}
