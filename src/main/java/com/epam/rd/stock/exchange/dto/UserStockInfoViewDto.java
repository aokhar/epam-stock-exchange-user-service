package com.epam.rd.stock.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStockInfoViewDto {
    private String id;
    private String symbol;
    private BigDecimal price;
    private BigDecimal trend;
    private Integer amount;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private Integer amountToSell;
}
