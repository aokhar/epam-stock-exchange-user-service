package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderFacade {
    OrderViewDto submit(OrderCreateDto orderCreateDto);

    Page<OrderViewDto> findByUserIdAndStatus(String userId, OrderStatus status, Integer page, int size);
}
