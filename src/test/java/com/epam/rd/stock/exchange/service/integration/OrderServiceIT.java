package com.epam.rd.stock.exchange.service.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.OrderNotFoundException;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.repository.OrderRepository;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import com.epam.rd.stock.exchange.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderServiceIT extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockRepository stockRepository;

    private Order order;

    private Pageable pageable;

    private User user;

    private Stock stock;

    @BeforeEach
    public void init() {
        order = EntityGenerator.generateDomainOrder();
        user = EntityGenerator.generateDomainUser();
        stock = EntityGenerator.generateDomainStock();
        pageable = EntityGenerator.generatePageable();
    }

    @Test
    public void shouldSaveOrderInRepository() {
        // Given

        // When
        Order savedOrder = service.save(order);

        // Then
        Order actualSavedOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertNotNull(actualSavedOrder);
        assertEquals(actualSavedOrder.getActualOrderPrice(), savedOrder.getActualOrderPrice());
        assertEquals(actualSavedOrder.getExpectedOrderPrice(), savedOrder.getExpectedOrderPrice());
        assertEquals(actualSavedOrder.getExpectedStockPrice(), savedOrder.getExpectedStockPrice());
        assertEquals(actualSavedOrder.getAmount(), savedOrder.getAmount());
        assertEquals(actualSavedOrder.getStatus(), savedOrder.getStatus());
        assertEquals(actualSavedOrder.getFailDescription(), savedOrder.getFailDescription());
    }

    @Test
    public void shouldThrowOrderNotFoundExceptionWhenFindById() {
        // Given
        orderRepository.delete(order);

        // When

        // Then
        assertThrows(OrderNotFoundException.class,
                () -> service.findById(order.getId()));
    }

    @Test
    public void shouldFindByUserIdAndStatus() {
        // Given

        // When
        User savedUser = userRepository.save(user);
        Stock savedStock = stockRepository.save(stock);

        order.setUser(savedUser);
        order.setStock(savedStock);

        Order savedOrder = service.save(order);

        // Then
        Page<Order> actualSavedOrders = orderRepository.findByUserAndStatusOrderByTimeSubmittedDesc(savedOrder.getUser(), savedOrder.getStatus(), pageable);
        System.out.println(actualSavedOrders.toString());
        assertEquals(actualSavedOrders.getContent().size(), 1);
    }

    @Test
    public void shouldFindByUser() {
        // Given

        // When
        User savedUser = userRepository.save(user);
        Stock savedStock = stockRepository.save(stock);

        order.setUser(savedUser);
        order.setStock(savedStock);

        Order savedOrder = service.save(order);

        // Then
        Page<Order> actualSavedOrders = orderRepository.findByUserOrderByTimeSubmittedDesc(savedOrder.getUser(), pageable);
        System.out.println(actualSavedOrders.toString());
        assertEquals(actualSavedOrders.getContent().size(), 1);
    }
}
