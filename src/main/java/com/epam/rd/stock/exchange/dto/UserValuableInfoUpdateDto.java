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
public class UserValuableInfoUpdateDto {
    private String id;
    private String valuableId;
    private String userId;
    private Integer amountToSell;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
}
