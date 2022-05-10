package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;

public interface WalletFacade {

    ChangeWalletBalanceDto findByUserId(String userId);

    void changeBalance(ChangeWalletBalanceDto changeWalletBalanceDto);

}
