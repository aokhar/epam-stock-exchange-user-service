package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.facade.WalletFacade;
import com.epam.rd.stock.exchange.mapper.WalletMapper;
import com.epam.rd.stock.exchange.model.Wallet;
import com.epam.rd.stock.exchange.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletFacadeImpl implements WalletFacade {

    private final WalletService walletService;

    private final WalletMapper walletMapper;

    @Override
    public ChangeWalletBalanceDto findByUserId(String userId) {
        return walletMapper.toViewDto(walletService.findByUserId(userId));
    }

    @Override
    public void changeBalance(ChangeWalletBalanceDto changeWalletBalanceDto) {
        Wallet wallet = walletService.save(updateCreditCardDetails(changeWalletBalanceDto));
        walletService.changeBalance(wallet.getUserId(), changeWalletBalanceDto.getSum());
    }

    private Wallet updateCreditCardDetails(ChangeWalletBalanceDto changeWalletBalanceDto) {
        Wallet updateWallet = walletService.findByUserId(changeWalletBalanceDto.getUserId());
        updateWallet.setCardHolder(changeWalletBalanceDto.getCardHolder());
        updateWallet.setExpMonth(changeWalletBalanceDto.getExpMonth());
        updateWallet.setExpYear(changeWalletBalanceDto.getExpYear());
        updateWallet.setCard(changeWalletBalanceDto.getCard());
        updateWallet.setCvc(changeWalletBalanceDto.getCvc());
        return updateWallet;
    }
}
