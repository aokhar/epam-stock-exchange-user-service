package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.ValuableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValuableInfoViewDto {
    private String id;
    private String symbol;
    private ValuableType type;
    private BigDecimal price;
    private BigDecimal trend;
    private Integer amount;
    private Integer amountToSell;
}
