package com.epam.rd.stock.exchange.service.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.repository.UserStockInfoRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import com.epam.rd.stock.exchange.service.UserStockInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserStockInfoIT extends AbstractIntegrationTest {

    @Autowired
    private UserStockInfoRepository userStockInfoRepository;
    @Autowired
    private UserStockInfoService userStockInfoService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;

    private UserStockInfo userStockInfo;
    private User user;
    private Stock stock;

    @BeforeEach
    public void init() {
        userStockInfo = EntityGenerator.generateUserStockInfo();
        user = EntityGenerator.generateDomainUser();
        stock = EntityGenerator.generateDomainStock();
    }

    @Test
    public void shouldSaveUserStockInfoInRepository() {
        // Given
        User savedUser = userRepository.save(user);
        Stock savedStock = stockRepository.save(stock);
        userStockInfo.setStock(savedStock);
        userStockInfo.setUser(savedUser);

        // When
        UserStockInfo savedUserStockInfo = userStockInfoService.save(userStockInfo);

        // Then
        UserStockInfo actualSavedUserStockInfo = userStockInfoRepository.findByUserIdAndStockId(savedUserStockInfo.getUser().getId(), savedUserStockInfo.getStock().getId()).orElse(null);
        assertNotNull(actualSavedUserStockInfo);
        assertEquals(savedUserStockInfo.getAmount(), actualSavedUserStockInfo.getAmount());
    }

    @Test
    public void shouldDeleteUserStockInfo() {
        // Given
        User savedUser = userRepository.save(user);
        Stock savedStock = stockRepository.save(stock);
        userStockInfo.setStock(savedStock);
        userStockInfo.setUser(savedUser);

        // When
        UserStockInfo savedUserStockInfo = userStockInfoService.save(userStockInfo);

        // Then
        userStockInfoService.delete(savedUserStockInfo);
        UserStockInfo actualSavedUserStockInfo = userStockInfoRepository.findByUserIdAndStockId(savedUserStockInfo.getUser().getId(), savedUserStockInfo.getStock().getId()).orElse(null);
        assertNull(actualSavedUserStockInfo);
    }
}
