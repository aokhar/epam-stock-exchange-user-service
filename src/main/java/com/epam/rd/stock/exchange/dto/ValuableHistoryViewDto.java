package com.epam.rd.stock.exchange.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ValuableHistoryViewDto {

    private BigDecimal previousPrice;

    private BigDecimal newPrice;

    private BigDecimal trend;

    private LocalDateTime dateTime;
}
