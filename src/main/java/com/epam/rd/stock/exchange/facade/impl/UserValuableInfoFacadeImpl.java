package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.UserValuableInfoUpdateDto;
import com.epam.rd.stock.exchange.facade.UserValuableInfoFacade;
import com.epam.rd.stock.exchange.mapper.UserValuableInfoMapper;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import com.epam.rd.stock.exchange.service.UserValuableInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValuableInfoFacadeImpl implements UserValuableInfoFacade {

    private final UserValuableInfoService userValuableInfoService;

    private final UserValuableInfoMapper userValuableInfoMapper;

    @Override
    public void updateUserValuableInfo(UserValuableInfoUpdateDto userValuableInfoUpdateDto) {
        UserValuableInfo userValuableInfo = userValuableInfoService.findById(userValuableInfoUpdateDto.getId());
        userValuableInfoService.save(userValuableInfo);
    }

    @Override
    public UserValuableInfoUpdateDto findById(String userStockInfoId) {
        return userValuableInfoMapper.toUserValuableInfoUpdateDto(userValuableInfoService.findById(userStockInfoId));
    }
}
