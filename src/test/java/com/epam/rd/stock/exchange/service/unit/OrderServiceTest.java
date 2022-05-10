package com.epam.rd.stock.exchange.service.unit;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.OrderNotFoundException;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.repository.OrderRepository;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.service.OrderService;
import com.epam.rd.stock.exchange.service.impl.OrderServiceImpl;
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

public class OrderServiceTest {

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderService orderService = new OrderServiceImpl(orderRepository, userRepository);

    private Order order;

    private Pageable pageable;

    @BeforeEach
    public void init() {
        order = EntityGenerator.generateDomainOrder();
        order.setUser(EntityGenerator.generateDomainUser());
        pageable = EntityGenerator.generatePageable();
    }

    @Test
    public void shouldSaveUser() {
        //Given
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        //When
        Order newOrder = orderService.save(order);

        //Then
        Assertions.assertEquals(newOrder, order);
    }

    @Test
    public void shouldReturnOrderNotFoundExceptionWhenFindById() {
        //Given
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        //When

        //Then
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.findById(order.getId()));
    }

    @Test
    public void shouldReturnByUserIdAndStatus() {
        //Given
        List<Order> stockList = new ArrayList<>();
        stockList.add(order);
        Page<Order> page = new PageImpl<>(stockList.subList(0, stockList.size()), pageable, stockList.size());
        when(orderRepository.findByUserAndStatusOrderByTimeSubmittedDesc(order.getUser(), order.getStatus(), pageable)).thenReturn(page);
        when(userRepository.findById(order.getUser().getId())).thenReturn(Optional.of(order.getUser()));

        //When
        Page<Order> newPageOrder = orderService.findByUserIdAndStatus(order.getUser().getId(), order.getStatus(), pageable);

        //Then
        Assertions.assertEquals(page, newPageOrder);
    }

    @Test
    public void shouldReturnByUserId() {
        //Given
        List<Order> stockList = new ArrayList<>();
        stockList.add(order);
        Page<Order> page = new PageImpl<>(stockList.subList(0, stockList.size()), pageable, stockList.size());
        when(orderRepository.findByUserOrderByTimeSubmittedDesc(order.getUser(), pageable)).thenReturn(page);
        when(userRepository.findById(order.getUser().getId())).thenReturn(Optional.of(order.getUser()));

        //When
        Page<Order> newPageOrder = orderService.findByUserId(order.getUser().getId(), pageable);

        //Then
        Assertions.assertEquals(page, newPageOrder);
    }
}
