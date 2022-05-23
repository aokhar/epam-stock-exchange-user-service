package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.Valuable;
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

    private Valuable valuable;

    private Integer amount;

    private BigDecimal orderPrice;

    private BigDecimal valuablePrice;

    private OrderType type;

    private LocalDateTime dateTime;

}
