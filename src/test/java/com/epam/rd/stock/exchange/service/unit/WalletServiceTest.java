package com.epam.rd.stock.exchange.service.unit;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.WalletNotFoundException;
import com.epam.rd.stock.exchange.service.WalletService;
import com.epam.rd.stock.exchange.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalletServiceTest {

    private WalletRepository repository = mock(WalletRepository.class);
    private WalletService service = new WalletServiceImpl(repository);

    private Wallet wallet;

    @BeforeEach
    public void init() {
        wallet = EntityGenerator.generateDomainWallet();
    }

    @Test
    public void shouldFindWalletByUserId() {
        // Given
        when(repository.findByUserId(wallet.getId())).thenReturn(Optional.of(wallet));

        // When
        Wallet returnedWallet = service.findByUserId(wallet.getId());

        // Then
        Assertions.assertNotNull(returnedWallet);
        Assertions.assertEquals(returnedWallet, wallet);
    }

    @Test
    public void shouldThrowWalletNotFoundExceptionWhenFindByUserId() {
        // Given
        when(repository.findByUserId(wallet.getId())).thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(WalletNotFoundException.class, () -> service.findByUserId(wallet.getId()));
    }


    @Test
    public void shouldChangeBalance() {
        // Given
        BigDecimal amountOld = wallet.getBalance();
        when(repository.findByUserId(wallet.getId())).thenReturn(Optional.of(wallet));
        when(repository.save(any())).thenReturn(wallet);

        // When
        Wallet returnedWallet = service.changeBalance(wallet.getId(), BigDecimal.valueOf(5d));

        // Then
        Assertions.assertNotEquals(amountOld, returnedWallet.getBalance());
    }

    @Test
    public void shouldThrowWalletNotFoundExceptionWhenChangeBalance() {
        // Given
        when(repository.findByUserId(wallet.getId())).thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(WalletNotFoundException.class, () -> service.changeBalance(wallet.getId(), BigDecimal.valueOf(5d)));
    }
}