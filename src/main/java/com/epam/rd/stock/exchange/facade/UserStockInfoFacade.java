package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;

public interface UserStockInfoFacade {

    void updateUserStockInfo(UserStockInfoUpdateDto userStockInfoUpdateDto);

    UserStockInfoUpdateDto findById(String userStockInfoId);
}
