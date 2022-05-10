package com.epam.rd.stock.exchange.controller.unit;

import com.epam.rd.stock.exchange.controller.UserStockInfoController;
import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.facade.UserStockInfoFacade;
import com.epam.rd.stock.exchange.handler.GlobalExceptionHandler;
import com.epam.rd.stock.exchange.mapper.StockMapper;
import com.epam.rd.stock.exchange.mapper.UserStockInfoMapper;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserStockInfoControllerTest {

    private MockMvc mockMvc;

    private StockFacade stockFacade = mock(StockFacade.class);

    private UserStockInfoFacade userStockInfoFacade = mock(UserStockInfoFacade.class);

    private UserStockInfoUpdateDto userStockInfoUpdateDto;

    private StockViewDto stockViewDto;

    @BeforeEach
    public void init() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserStockInfoController(stockFacade, userStockInfoFacade))
                .setViewResolvers(viewResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        UserStockInfoMapper userStockInfoMapper = new UserStockInfoMapper();
        StockMapper stockMapper = new StockMapper();
        User user = EntityGenerator.generateDomainUser();
        Stock stock = EntityGenerator.generateDomainStock();
        UserStockInfo userStockInfo = EntityGenerator.generateUserStockInfo();
        userStockInfo.setUser(user);
        userStockInfo.setStock(stock);
        userStockInfoUpdateDto = userStockInfoMapper.toUserStockInfoUpdateDto(userStockInfo);
        stockViewDto = stockMapper.toStockDto(stock);
    }

    @Test
    public void shouldGetUpdateUserStockInfoPage() throws Exception {
        //GIVEN
        when(userStockInfoFacade.findById(userStockInfoUpdateDto.getId())).thenReturn(userStockInfoUpdateDto);
        when(stockFacade.findById(userStockInfoUpdateDto.getStockId())).thenReturn(stockViewDto);

        //WHEN + THEN
        mockMvc.perform(get("/updateUserStockInfo").param("userStockInfoId", userStockInfoUpdateDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("updateUserStockInfo"))
                .andExpect(model().attributeExists("userStockInfo", "stock"));
    }

    @Test
    public void shouldUpdateUserStockInfo() throws Exception {
        //GIVEN

        //WHEN + THEN
        mockMvc.perform(post("/updateUserStockInfo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/portfolio"));
    }
}
