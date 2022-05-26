package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {
    private String userEmail;
    @NotNull(message = "Valuable id must not be blank")
    private String valuableId;
    @NotNull(message = "Amount must not be blank!")
    @Positive(message = "Amount must be higher than 0!")
    private BigDecimal amount;
    @NotNull(message = "Order type must not be blank!")
    private OrderType type;
}
