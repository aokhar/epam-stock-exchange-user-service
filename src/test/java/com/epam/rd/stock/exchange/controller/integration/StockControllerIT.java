package com.epam.rd.stock.exchange.controller.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class StockControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void shouldReturnStocks() throws Exception {
        //GIVEN
        Stock stock1 = EntityGenerator.generateDomainStock();
        Stock stock2 = EntityGenerator.generateDomainStock();
        Stock stock3 = EntityGenerator.generateDomainStock();

        stockRepository.save(stock1);
        stockRepository.save(stock2);
        stockRepository.save(stock3);

        //WHEN + THEN
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("stocks", hasSize(3)));
    }
}
