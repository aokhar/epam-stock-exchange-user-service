package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toOrder(Valuable valuable, User user, OrderCreateDto orderCreateDto) {
        return Order.builder()
                .amount(orderCreateDto.getAmount())
                .valuablePrice(orderCreateDto.getValuablePrice())
                .valuable(valuable)
                .user(user)
                .type(orderCreateDto.getType())
                .build();
    }

    public OrderViewDto toOrderDto(Order order) {
        return OrderViewDto.builder()
                .id(order.getId())
                .valuable(order.getValuable())
                .user(order.getUser())
                .orderPrice(order.getOrderPrice())
                .valuablePrice(order.getValuablePrice())
                .type(order.getType())
                .amount(order.getAmount())
                .dateTime(order.getDateTime())
                .build();
    }

    public Page<OrderViewDto> toPageOrderDto(Page<Order> orders) {
        return orders.map(this::toOrderDto);
    }

}
