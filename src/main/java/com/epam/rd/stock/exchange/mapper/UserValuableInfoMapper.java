package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.UserValuableInfoUpdateDto;
import com.epam.rd.stock.exchange.dto.UserValuableInfoViewDto;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import org.springframework.stereotype.Component;

@Component
public class UserValuableInfoMapper {
    public UserValuableInfoViewDto toUserValuableInfoViewDto(UserValuableInfo userStockInfo) {
        return UserValuableInfoViewDto.builder()
                .id(userStockInfo.getId())
                .type(userStockInfo.getValuable().getType())
                .valuableId(userStockInfo.getValuable().getId())
                .amount(userStockInfo.getAmount())
                .price(userStockInfo.getValuable().getPrice())
                .amountToSell(userStockInfo.getSellAmount())
                .symbol(userStockInfo.getValuable().getSymbol())
                .trend(userStockInfo.getValuable().getTrend())
                .build();
    }

    public UserValuableInfoUpdateDto toUserValuableInfoUpdateDto(UserValuableInfo userStockInfo) {
        return UserValuableInfoUpdateDto.builder()
                .id(userStockInfo.getId())
                .valuableId(userStockInfo.getValuable().getId())
                .userId(userStockInfo.getUser().getId())
                .build();
    }
}
