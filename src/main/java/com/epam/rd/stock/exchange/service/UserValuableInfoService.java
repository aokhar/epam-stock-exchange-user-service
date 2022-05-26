package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.UserValuableInfo;

import java.util.List;


public interface UserValuableInfoService {
    UserValuableInfo save(UserValuableInfo userValuableInfo);

    void delete(UserValuableInfo uservaluableInfo);

    UserValuableInfo findUserStockInfo(String userId, String valuableId);

    List<UserValuableInfo> findByUserId(String userId);

    UserValuableInfo findById(String userValuableInfoId);

    List<UserValuableInfo> findAll();
}
