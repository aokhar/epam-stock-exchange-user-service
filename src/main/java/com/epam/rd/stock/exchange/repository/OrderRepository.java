package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findByUserAndStatusOrderByDateTimeDesc(User user, OrderStatus status, Pageable pageable);

    Page<Order> findByUserOrderByDateTimeDesc(User user, Pageable pageable);

    List<Order> findByStatus(OrderStatus status);
}
