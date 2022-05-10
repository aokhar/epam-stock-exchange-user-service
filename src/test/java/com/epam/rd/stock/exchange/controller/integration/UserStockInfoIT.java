package com.epam.rd.stock.exchange.controller.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;
import com.epam.rd.stock.exchange.mapper.UserStockInfoMapper;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.repository.UserStockInfoRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserStockInfoIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserStockInfoRepository userStockInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    private User user;
    private Stock stock;
    private UserStockInfo userStockInfo;
    private UserStockInfoMapper userStockInfoMapper;

    @BeforeEach
    public void init() {
        userStockInfoMapper = new UserStockInfoMapper();
        user = EntityGenerator.generateDomainUser();
        stock = EntityGenerator.generateDomainStock();
        userStockInfo = EntityGenerator.generateUserStockInfo();
    }

    @Test
    public void shouldGetUpdateUserStockInfoPage() throws Exception {
        //GIVEN
        User repoUser = userRepository.save(user);
        Stock repoStock = stockRepository.save(stock);
        userStockInfo.setStock(repoStock);
        userStockInfo.setUser(repoUser);
        UserStockInfo repoUserStockInfo = userStockInfoRepository.save(userStockInfo);
        UserStockInfoUpdateDto userStockInfoUpdateDto = userStockInfoMapper.toUserStockInfoUpdateDto(repoUserStockInfo);

        //WHEN + THEN
        mockMvc.perform(get("/updateUserStockInfo").param("userStockInfoId", userStockInfoUpdateDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("updateUserStockInfo"))
                .andExpect(model().attributeExists("userStockInfo", "stock"));
    }
}
