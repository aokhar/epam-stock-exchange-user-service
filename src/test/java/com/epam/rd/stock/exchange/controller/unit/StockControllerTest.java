package com.epam.rd.stock.exchange.controller.unit;

import com.epam.rd.stock.exchange.controller.StockController;
import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.handler.GlobalExceptionHandler;
import com.epam.rd.stock.exchange.mapper.StockMapper;
import com.epam.rd.stock.exchange.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class StockControllerTest {

    private MockMvc mockMvc;

    private StockFacade stockFacade = mock(StockFacade.class);

    private Page<StockViewDto> pageStocks;
    private StockMapper stockMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new StockController(stockFacade))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        Stock stock1 = EntityGenerator.generateDomainStock();
        Stock stock2 = EntityGenerator.generateDomainStock();
        Stock stock3 = EntityGenerator.generateDomainStock();
        List<Stock> stockList = new ArrayList();
        stockList.add(stock1);
        stockList.add(stock2);
        stockList.add(stock3);
        stockMapper = new StockMapper();
        pageStocks = stockMapper.toPageStockDto(new PageImpl<>(stockList));
    }

    @Test
    public void shouldReturnStocks() throws Exception {
        //Given
        when(stockFacade.findStocksBySymbol(anyString(), anyInt(), anyInt())).thenReturn(pageStocks);

        //When + Then
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("stocks", hasSize(3)));
    }
}
