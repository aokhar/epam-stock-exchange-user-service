package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toOrder(Stock stock, User user, OrderCreateDto orderCreateDto) {
        return Order.builder()
                .amount(orderCreateDto.getAmount())
                .expectedStockPrice(orderCreateDto.getExpectedStockPrice())
                .stock(stock)
                .user(user)
                .type(orderCreateDto.getType())
                .build();
    }

    public OrderViewDto toOrderDto(Order order) {
        return OrderViewDto.builder()
                .id(order.getId())
                .stock(order.getStock())
                .user(order.getUser())
                .actualOrderPrice(order.getActualOrderPrice())
                .expectedStockPrice(order.getExpectedStockPrice())
                .expectedOrderPrice(order.getExpectedOrderPrice())
                .status(order.getStatus())
                .type(order.getType())
                .amount(order.getAmount())
                .timeSubmitted(order.getTimeSubmitted())
                .timeProcessed(order.getTimeProcessed())
                .failDescription(order.getFailDescription())
                .build();
    }

    public Page<OrderViewDto> toPageOrderDto(Page<Order> orders) {
        return orders.map(this::toOrderDto);
    }

}
