package com.epam.rd.stock.exchange.service.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.StockNotFoundException;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import com.epam.rd.stock.exchange.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockServiceIT extends AbstractIntegrationTest {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockService sut;

    private Stock stock;
    private Pageable pageable;

    @BeforeEach
    public void init() {
        stock = EntityGenerator.generateDomainStock();
        pageable = EntityGenerator.generatePageable();
    }

    @Test
    public void shouldReturnStockWhenFindById() {
        //Given

        //When
        Stock stockFromRepo = stockRepository.save(stock);

        //Then
        Stock actualSavedStock = sut.findById(stockFromRepo.getId());
        assertNotNull(actualSavedStock);
        assertEquals(stock.getPrice(), actualSavedStock.getPrice());
        assertEquals(stock.getSymbol(), actualSavedStock.getSymbol());
        assertEquals(stock.getTrend(), actualSavedStock.getTrend());
    }

    @Test
    public void shouldThrowStockNotFoundExceptionWhenFindById() {
        //Given
        stockRepository.delete(stock);
        //When

        //Then
        assertThrows(StockNotFoundException.class,
                () -> sut.findById(stock.getId()));
    }

    @Test
    public void shouldFindStocksBySymbol() {
        //Given

        //When
        Stock stockFromRepo = stockRepository.save(stock);


        //Then
        Page<Stock> actualSavedStocks = sut.findStocksBySymbol(stockFromRepo.getSymbol(), pageable);
        assertEquals(actualSavedStocks.getContent().size(), 1);
    }
}
