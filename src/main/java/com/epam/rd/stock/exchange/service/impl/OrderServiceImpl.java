package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.OrderNotFoundException;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.repository.OrderRepository;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findById(String id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order with id " + id + " does not exist."));
    }

    @Override
    public Page<Order> findByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable) {
        return orderRepository.findByUserAndStatusOrderByDateTimeDesc(userRepository.findById(userId).get(), status, pageable);
    }

    @Override
    public Page<Order> findByUserId(String userId, Pageable pageable) {
        return orderRepository.findByUserOrderByDateTimeDesc(userRepository.findById(userId).get(), pageable);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
}
