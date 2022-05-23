package com.epam.rd.stock.exchange.service.unit;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.ValuableNotFoundException;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.service.StockService;
import com.epam.rd.stock.exchange.service.impl.StockServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockServiceTest {

    private StockRepository repository = mock(StockRepository.class);

    private StockService service = new StockServiceImpl(repository);

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
        when(repository.save(stock)).thenReturn(stock);
        when(repository.findById(stock.getId())).thenReturn(Optional.of(stock));

        //When
        Stock newStock = service.findById(stock.getId());

        //Then
        Assertions.assertEquals(stock, newStock);
    }

    @Test
    public void shouldThrowStockNotFoundExceptionWhenFindById() {
        //Given
        when(repository.findById(stock.getId())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThrows(ValuableNotFoundException.class, () -> service.findById(stock.getId()));
    }

    @Test
    public void shouldReturnStocksBySymbol() {
        //Given
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock);
        Page<Stock> page = new PageImpl<>(stockList.subList(0, stockList.size()), pageable, stockList.size());
        when(repository.findStocksBySymbol(stock.getSymbol(), pageable)).thenReturn(page);

        //When
        Page<Stock> newPageStock = service.findStocksBySymbol(stock.getSymbol(), pageable);

        //Then
        Assertions.assertEquals(page, newPageStock);
    }
}
