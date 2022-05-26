package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.UserValuableInfoUpdateDto;

public interface UserValuableInfoFacade {

    void updateUserValuableInfo(UserValuableInfoUpdateDto userValuableInfoUpdateDto);

    UserValuableInfoUpdateDto findById(String userStockInfoId);
}
