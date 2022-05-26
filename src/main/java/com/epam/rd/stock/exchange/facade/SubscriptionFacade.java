package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.CreateSubscriptionDto;
import com.epam.rd.stock.exchange.dto.SubscriptionViewDto;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import org.springframework.data.domain.Page;

public interface SubscriptionFacade {
    SubscriptionViewDto createSubscription(String email, CreateSubscriptionDto createSubscription);
    void deleteSubscription(String email, String subscriptionId);
    Page<SubscriptionViewDto> get(String email, int page, int pageSize);
    Page<SubscriptionViewDto> get(String email, SubscriptionType subscriptionType, int page, int pageSize);
}
