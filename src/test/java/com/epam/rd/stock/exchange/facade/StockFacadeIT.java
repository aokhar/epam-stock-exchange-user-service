package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StockFacadeIT extends AbstractIntegrationTest {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockFacade stockFacade;

    private Stock stock;
    private Pageable pageable;

    @BeforeEach
    public void init() {
        stock = EntityGenerator.generateDomainStock();
        pageable = EntityGenerator.generatePageable();
    }

    @Test
    public void shouldFindById() {
        // Given
        Stock repoStock = stockRepository.save(stock);

        // When
        StockViewDto newStockViewDto = stockFacade.findById(repoStock.getId());

        // Then
        assertNotNull(newStockViewDto);
        assertEquals(newStockViewDto.getPrice(), repoStock.getPrice());
        assertEquals(newStockViewDto.getSymbol(), repoStock.getSymbol());
        assertEquals(newStockViewDto.getTrend(), repoStock.getTrend());
    }

    @Test
    public void shouldReturnStockBySymbol() {
        // Given
        Stock repoStock = stockRepository.save(stock);

        // When
        Page<StockViewDto> stockViewDto = stockFacade.findStocksBySymbol(repoStock.getSymbol(), 1, pageable.getPageSize());

        // Then
        assertEquals(stockViewDto.getContent().size(), 1);
    }
}
