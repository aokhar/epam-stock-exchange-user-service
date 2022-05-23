package com.epam.rd.stock.exchange.service;

import java.math.BigDecimal;

public interface WalletService {

    Wallet changeBalance(String userId, BigDecimal number);

    Wallet findByUserId(String userId);

    Wallet save(Wallet wallet);

}
