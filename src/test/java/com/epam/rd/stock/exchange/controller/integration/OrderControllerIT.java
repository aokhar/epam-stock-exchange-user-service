package com.epam.rd.stock.exchange.controller.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class OrderControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockFacade stockFacade;

    private Stock stock;

    @BeforeEach
    public void init() {
        stock = EntityGenerator.generateDomainStock();
    }

    @Test
    public void shouldReturnNewOrderPage() throws Exception {
        //Given
        Stock repoStock = stockRepository.save(stock);

        //When
        ValuableViewDto valuableViewDto = stockFacade.findById(repoStock.getId());

        //Then
        mockMvc.perform(get("/order").param("stockId", valuableViewDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("newOrder"))
                .andExpect(model().attributeExists("stock", "newOrder"));
    }
}
