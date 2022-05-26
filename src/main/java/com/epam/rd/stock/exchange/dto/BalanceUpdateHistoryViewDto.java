package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceUpdateHistoryViewDto {

    private BigDecimal update;

    private BalanceUpdateType type;

    private LocalDateTime dateTime;

    private Long card;
}
