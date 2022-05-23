package com.epam.rd.stock.exchange.service.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.WalletNotFoundException;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import com.epam.rd.stock.exchange.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletServiceIT extends AbstractIntegrationTest {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService wut;

    private Wallet wallet;

    @BeforeEach
    public void init() {
        wallet = EntityGenerator.generateDomainWallet();
    }

    @Test
    public void shouldSaveWalletInRepository() {
        // Given

        // When
        wut.save(wallet);

        // Then
        Wallet actualSavedWallet = walletRepository.findByUserId(wallet.getUserId()).orElse(null);
        assertNotNull(actualSavedWallet);
        assertEquals(wallet.getBalance(), actualSavedWallet.getBalance());
        assertEquals(wallet.getCard(), actualSavedWallet.getCard());
        assertEquals(wallet.getCardHolder(), actualSavedWallet.getCardHolder());
        assertEquals(wallet.getUserId(), actualSavedWallet.getUserId());
        assertEquals(wallet.getCvc(), actualSavedWallet.getCvc());
        assertEquals(wallet.getExpMonth(), actualSavedWallet.getExpMonth());
        assertEquals(wallet.getExpYear(), actualSavedWallet.getExpYear());
    }

    @Test
    public void shouldThrowWalletNotFoundExceptionWhenFindById() {
        // Given
        walletRepository.delete(wallet);

        // When

        // Then
        assertThrows(WalletNotFoundException.class,
                () -> wut.findByUserId(wallet.getUserId()));
    }

    @Test
    public void shouldChangeWalletBalance() {
        // Given
        BigDecimal addAmount = BigDecimal.valueOf(50d);

        // When
        wut.save(wallet);

        // Then
        Wallet actualSavedWallet = wut.changeBalance(wallet.getUserId(), addAmount);
        assertEquals(wallet.getBalance().add(addAmount), actualSavedWallet.getBalance());
    }

    @Test
    public void shouldThroughWalletNotFoundExceptionWhenWalletNotExist() {
        // Given
        BigDecimal addAmount = BigDecimal.valueOf(50d);
        walletRepository.delete(wallet);

        // When

        // Then
        assertThrows(WalletNotFoundException.class,
                () -> wut.changeBalance(wallet.getUserId(), addAmount));
    }

}
