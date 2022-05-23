package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.ValuableType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ValuableViewDto {
    private String id;

    private String symbol;

    private String name;

    private BigDecimal price;

    private BigDecimal trend;

    private ValuableType type;
}
