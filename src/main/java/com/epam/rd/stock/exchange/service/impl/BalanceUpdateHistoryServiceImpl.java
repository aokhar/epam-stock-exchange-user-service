package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.dto.enums.ChangeBalanceType;
import com.epam.rd.stock.exchange.model.BalanceUpdateHistory;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import com.epam.rd.stock.exchange.repository.BalanceUpdateHistoryRepository;
import com.epam.rd.stock.exchange.service.BalanceUpdateHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BalanceUpdateHistoryServiceImpl implements BalanceUpdateHistoryService {

    private final BalanceUpdateHistoryRepository balanceUpdateHistoryRepository;

    @Override
    public Page<BalanceUpdateHistory> get(String userId, BalanceUpdateType changeBalanceType, Pageable pageable) {
        Page<BalanceUpdateHistory> history;
        if(changeBalanceType == null){
            history = balanceUpdateHistoryRepository.getBalanceUpdateHistoryByUserIdOrderByDateTimeDesc(userId, pageable);
        }
        else{
            history = balanceUpdateHistoryRepository.getBalanceUpdateHistoryByUserIdAndTypeOrderByDateTimeDesc(userId, changeBalanceType, pageable);
        }
        return history;
    }

    @Override
    public void save(BalanceUpdateHistory balanceUpdateHistory) {
        balanceUpdateHistoryRepository.save(balanceUpdateHistory);
    }
}
