package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.model.Valuable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ValuableMapper {
    public ValuableViewDto toStockDto(Valuable valuable) {
        return ValuableViewDto.builder()
                .id(valuable.getId())
                .price(valuable.getPrice())
                .symbol(valuable.getSymbol())
                .name(valuable.getName())
                .trend(valuable.getTrend())
                .type(valuable.getType())
                .build();
    }

    public Page<ValuableViewDto> toPageStockDto(Page<Valuable> valuables) {
        return valuables.map(this::toStockDto);
    }
}
