package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.WalletNotFoundException;
import com.epam.rd.stock.exchange.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet findByUserId(String userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet with user id " + userId + " not exists"));
    }

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Transactional
    @Override
    public Wallet changeBalance(String userId, BigDecimal number) {
        if (!isWalletExists(userId)) {
            throw new WalletNotFoundException("Wallet with user id " + userId + " not exists");
        }
        Wallet wallet = findByUserId(userId);
        wallet.setBalance(wallet.getBalance().add(number));
        return walletRepository.save(wallet);
    }

    private boolean isWalletExists(String userId) {
        Optional<Wallet> optionalUser = walletRepository.findByUserId(userId);
        return optionalUser.isPresent();
    }

}
