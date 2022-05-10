package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.enums.OrderType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderViewDto {
    private String id;

    private User user;

    private Stock stock;

    private Integer amount;

    private OrderStatus status;

    private BigDecimal expectedOrderPrice;

    private BigDecimal expectedStockPrice;

    private BigDecimal actualOrderPrice;

    private OrderType type;

    private LocalDateTime timeSubmitted;

    private LocalDateTime timeProcessed;

    private String failDescription;
}
