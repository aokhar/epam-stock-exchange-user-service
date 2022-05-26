package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.ConditionType;
import com.epam.rd.stock.exchange.model.enums.Operator;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionDto {
    private SubscriptionType subscriptionType;
    private ConditionType conditionType;
    private String valuableId;
    private BigDecimal amount;
    private BigDecimal price;
    private Operator operator;
    private BigDecimal high;
    private BigDecimal low;
    private boolean failSafe;
    private boolean reserve;
    private boolean continuos;
}
