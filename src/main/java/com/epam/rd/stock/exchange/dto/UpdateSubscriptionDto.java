package com.epam.rd.stock.exchange.dto;

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
public class UpdateSubscriptionDto {
    private SubscriptionType subscriptionType;
    private BigDecimal amount;
    private String valuableId;
    private String subscriptionId;
    private boolean reserve;
    private boolean failSafe;
}
