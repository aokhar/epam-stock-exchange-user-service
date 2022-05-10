package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.UserStockInfo;

import java.util.List;


public interface UserStockInfoService {
    UserStockInfo save(UserStockInfo userStockInfo);

    void delete(UserStockInfo userStockInfo);

    UserStockInfo findUserStockInfo(String userId, String stockId);

    List<UserStockInfo> findByUserId(String userId);

    UserStockInfo findById(String userStockInfoId);

    List<UserStockInfo> findAll();
}
