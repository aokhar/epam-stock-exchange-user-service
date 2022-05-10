package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Order save(Order order);

    Order findById(String orderId);

    Page<Order> findByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable);

    Page<Order> findByUserId(String userId, Pageable pageable);

    List<Order> findByStatus(OrderStatus active);
}
