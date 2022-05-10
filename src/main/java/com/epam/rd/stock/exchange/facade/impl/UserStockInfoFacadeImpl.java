package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;
import com.epam.rd.stock.exchange.facade.UserStockInfoFacade;
import com.epam.rd.stock.exchange.mapper.UserStockInfoMapper;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.service.UserStockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStockInfoFacadeImpl implements UserStockInfoFacade {

    private final UserStockInfoService userStockInfoService;

    private final UserStockInfoMapper userStockInfoMapper;

    @Override
    public void updateUserStockInfo(UserStockInfoUpdateDto userStockInfoUpdateDto) {
        UserStockInfo userStockInfo = userStockInfoService.findById(userStockInfoUpdateDto.getId());
        userStockInfo.setAmountToSell(userStockInfoUpdateDto.getAmountToSell());
        userStockInfo.setStopLoss(userStockInfoUpdateDto.getStopLoss());
        userStockInfo.setTakeProfit(userStockInfoUpdateDto.getTakeProfit());
        userStockInfoService.save(userStockInfo);
    }

    @Override
    public UserStockInfoUpdateDto findById(String userStockInfoId) {
        return userStockInfoMapper.toUserStockInfoUpdateDto(userStockInfoService.findById(userStockInfoId));
    }
}
