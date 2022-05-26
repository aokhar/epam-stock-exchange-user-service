package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.CreateSubscriptionDto;
import com.epam.rd.stock.exchange.dto.SubscriptionViewDto;
import com.epam.rd.stock.exchange.dto.UpdateSubscriptionDto;
import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.service.ValuableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final ValuableService valuableService;

    public SubscriptionViewDto toUserView(Subscription subscription){
        Valuable valuable = valuableService.findById(subscription.getValuableId());
        return SubscriptionViewDto.builder()
                .id(subscription.getId())
                .amount(subscription.getAmount())
                .failSafe(subscription.isFailSafe())
                .reserve(subscription.isReserve())
                .continuos(subscription.isContinuos())
                .condition(subscription.conditionToString())
                .symbol(valuable.getSymbol())
                .type(subscription.getType())
                .valuableType(valuable.getType())
                .build();

    }

    public Subscription toSubscription(CreateSubscriptionDto createSubscriptionDto, User user, Valuable valuable){
        return Subscription.builder()
                .amount(createSubscriptionDto.getAmount())
                .userId(user.getId())
                .valuableId(valuable.getId())
                .price(createSubscriptionDto.getPrice())
                .operator(createSubscriptionDto.getOperator())
                .low(createSubscriptionDto.getLow())
                .continuos(createSubscriptionDto.isContinuos())
                .high(createSubscriptionDto.getHigh())
                .conditionType(createSubscriptionDto.getConditionType())
                .failSafe(createSubscriptionDto.isFailSafe())
                .reserve(createSubscriptionDto.isReserve())
                .type(createSubscriptionDto.getSubscriptionType())
                .build();
    }

}