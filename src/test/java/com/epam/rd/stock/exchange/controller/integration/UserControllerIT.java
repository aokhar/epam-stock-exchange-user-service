package com.epam.rd.stock.exchange.controller.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.mapper.WalletMapper;
import com.epam.rd.stock.exchange.model.Wallet;
import com.epam.rd.stock.exchange.repository.WalletRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    private Wallet wallet;
    private WalletMapper walletMapper;

    @BeforeEach
    public void init() {
        walletMapper = new WalletMapper();
        wallet = EntityGenerator.generateDomainWallet();
    }

    @Test
    public void shouldCallUpdateBalancePage() throws Exception {
        //GIVEN
        wallet.setCvc(123);
        wallet.setExpYear(1234);
        wallet.setExpMonth(12);
        wallet.setCard(11112222333344L);
        Wallet walletRepo = walletRepository.save(wallet);

        ChangeWalletBalanceDto changeWalletBalanceDto = walletMapper.toViewDto(walletRepo);
        changeWalletBalanceDto.setSum(BigDecimal.valueOf(100));

        //WHEN + THEN
        mockMvc.perform(post("/updateBalance").flashAttr("walletUpdate", changeWalletBalanceDto).sessionAttr("balance", changeWalletBalanceDto.getSum()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/wallet"));
    }
}
