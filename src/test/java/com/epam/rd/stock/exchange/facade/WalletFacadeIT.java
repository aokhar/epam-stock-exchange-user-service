package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.mapper.WalletMapper;
import com.epam.rd.stock.exchange.model.Wallet;
import com.epam.rd.stock.exchange.repository.WalletRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WalletFacadeIT extends AbstractIntegrationTest {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletMapper walletMapper;

    @Autowired
    WalletFacade walletFacade;

    private ChangeWalletBalanceDto walletBalanceDto;
    private Wallet domainWallet;

    @BeforeEach
    public void init() {
        domainWallet = EntityGenerator.generateDomainWallet();
        walletBalanceDto = walletMapper.toViewDto(domainWallet);
    }

    @Test
    public void shouldFindByUserId() {
        // Given
        walletRepository.save(domainWallet);

        // When
        ChangeWalletBalanceDto walletBalanceDto = walletFacade.findByUserId(domainWallet.getUserId());

        // Then
        assertNotNull(walletBalanceDto);
        assertEquals(walletBalanceDto.getBalance(), domainWallet.getBalance());
        assertEquals(walletBalanceDto.getCard(), domainWallet.getCard());
        assertEquals(walletBalanceDto.getCvc(), domainWallet.getCvc());
        assertEquals(walletBalanceDto.getCardHolder(), domainWallet.getCardHolder());
    }

    @Test
    public void shouldChangeBalance() {
        // Given
        BigDecimal sum = BigDecimal.valueOf(50d);
        Wallet returnedWallet = walletRepository.save(domainWallet);
        BigDecimal oldBalance = returnedWallet.getBalance();
        ChangeWalletBalanceDto newWalletBalanceDto = walletMapper.toViewDto(returnedWallet);
        newWalletBalanceDto.setSum(sum);

        // When
        walletFacade.changeBalance(newWalletBalanceDto);

        // Then
        assertEquals(walletRepository.findByUserId(newWalletBalanceDto.getUserId()).get().getBalance(), oldBalance.add(sum));
    }
}
