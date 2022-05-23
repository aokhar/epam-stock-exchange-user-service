package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.UserValuableInfoNotFoundException;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.repository.UserStockInfoRepository;
import com.epam.rd.stock.exchange.service.UserStockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStockInfoServiceImpl implements UserStockInfoService {

    private final UserStockInfoRepository userStockInfoRepository;

    @Override
    public UserStockInfo save(UserStockInfo userStockInfo) {
        return userStockInfoRepository.save(userStockInfo);
    }

    @Override
    public void delete(UserStockInfo userStockInfo) {
        userStockInfoRepository.delete(userStockInfo);
    }

    @Override
    public UserStockInfo findUserStockInfo(String userId, String stockId) {
        return userStockInfoRepository.findByUserIdAndStockId(userId, stockId).orElse(null);
    }

    @Override
    public List<UserStockInfo> findByUserId(String userId) {
        return userStockInfoRepository.findByUserId(userId);
    }


    @Override
    public UserStockInfo findById(String userStockInfoId) {
        return userStockInfoRepository.findById(userStockInfoId).orElseThrow(() -> new UserValuableInfoNotFoundException("UserStockInfo with id " + userStockInfoId + " not found."));
    }

    @Override
    public List<UserStockInfo> findAll() {
        return userStockInfoRepository.findAll();
    }
}
