package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.UserValuableInfoNotFoundException;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import com.epam.rd.stock.exchange.repository.UserValuableInfoRepository;
import com.epam.rd.stock.exchange.service.UserValuableInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserValuableInfoServiceImpl implements UserValuableInfoService {

    private final UserValuableInfoRepository userValuableInfoRepository;

    @Override
    public UserValuableInfo save(UserValuableInfo userStockInfo) {
        return userValuableInfoRepository.save(userStockInfo);
    }

    @Override
    public void delete(UserValuableInfo userStockInfo) {
        userValuableInfoRepository.delete(userStockInfo);
    }

    @Override
    public UserValuableInfo findUserStockInfo(String userId, String stockId) {
        return userValuableInfoRepository.findByUserIdAndValuableId(userId, stockId).orElse(null);
    }

    @Override
    public List<UserValuableInfo> findByUserId(String userId) {
        return userValuableInfoRepository.findByUserId(userId);
    }


    @Override
    public UserValuableInfo findById(String userStockInfoId) {
        return userValuableInfoRepository.findById(userStockInfoId).orElseThrow(() -> new UserValuableInfoNotFoundException("UserStockInfo with id " + userStockInfoId + " not found."));
    }

    @Override
    public List<UserValuableInfo> findAll() {
        return userValuableInfoRepository.findAll();
    }
}
