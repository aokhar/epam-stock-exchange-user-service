package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.ConditionType;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
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
public class SubscriptionViewDto {
    private String id;
    private String symbol;
    private BigDecimal amount;
    private String condition;
    private ConditionType conditionType;
    private ValuableType valuableType;
    private SubscriptionType type;
    private boolean failSafe;
    private boolean reserve;
    private boolean continuos;
}
