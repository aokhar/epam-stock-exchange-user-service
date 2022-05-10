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
    private String stockId;
    private String userEmail;
    @NotNull(message = "Amount must not be blank!")
    @Positive(message = "Amount must be higher than 0!")
    private Integer amount;
    private OrderType type;
    @NotNull(message = "Price must not be blank!")
    @Positive(message = "Price must be higher than 0!")
    private BigDecimal expectedStockPrice;
}
