package com.epam.rd.stock.exchange.controller.unit;

import com.epam.rd.stock.exchange.controller.OrderController;
import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.StockViewDto;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.handler.GlobalExceptionHandler;
import com.epam.rd.stock.exchange.mapper.StockMapper;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class OrderControllerTest {

    private MockMvc mockMvc;

    private Authentication auth = mock(Authentication.class);

    private StockFacade stockFacade = mock(StockFacade.class);

    private OrderFacade orderFacade = mock(OrderFacade.class);

    private StockMapper stockMapper = mock(StockMapper.class);

    private OrderCreateDto orderCreateDto;
    private StockViewDto stockViewDto;
    private User user;
    private Stock stock;
    private Order order;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OrderController(orderFacade, stockFacade))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        stockMapper = new StockMapper();
        user = EntityGenerator.generateDomainUser();
        stock = EntityGenerator.generateDomainStock();
        order = EntityGenerator.generateDomainOrder();
        order.setStock(stock);
        order.setUser(user);
        stockViewDto = stockMapper.toStockDto(stock);
        orderCreateDto = toOrderCreateDto(order);
    }

    @Test
    public void shouldReturnNewOrderPage() throws Exception {
        //Given
        when(stockFacade.findById(stockViewDto.getId())).thenReturn(stockViewDto);

        //When + Then
        mockMvc.perform(get("/order").param("stockId", stockViewDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("newOrder"))
                .andExpect(model().attributeExists("stock", "newOrder"));
    }

    @Test
    public void shouldSubmitOrderWithWrongModelAndGetError() throws Exception {
        //Given
        when(stockFacade.findById(stockViewDto.getId())).thenReturn(stockViewDto);
        orderCreateDto.setAmount(-1);

        //When + Then
        mockMvc.perform(post("/order").flashAttr("newOrder", orderCreateDto).principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("newOrder"))
                .andExpect(model().attributeExists("stock", "newOrder"));
    }

    @Test
    public void shouldSubmitOrder() throws Exception {
        //Given
        when(auth.getName()).thenReturn(user.getEmail());
        orderCreateDto.setAmount(123);
        orderCreateDto.setExpectedStockPrice(BigDecimal.valueOf(123.0));

        //When + Then
        mockMvc.perform(post("/order").flashAttr("newOrder", orderCreateDto).principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/portfolio"));
    }

    @Test
    public void shouldCancelOrder() throws Exception {
        //Given
        doNothing().when(orderFacade).cancel(order.getId());

        //When + Then
        mockMvc.perform(get("/cancelOrder").param("orderId", order.getId()).principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders"));
    }


    private OrderCreateDto toOrderCreateDto(Order order) {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setStockId(order.getStock().getId());
        orderCreateDto.setExpectedStockPrice(order.getExpectedStockPrice());
        orderCreateDto.setType(order.getType());
        orderCreateDto.setUserEmail(order.getUser().getEmail());
        orderCreateDto.setAmount(order.getAmount());
        return orderCreateDto;
    }
}
