package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionService {
    Subscription createSubscription(Subscription subscription);
    void deleteSubscription(User user, String subscriptionId);
    Subscription get(User user, String subscriptionId);
    Page<Subscription> get(User user, Pageable pageable);
    Page<Subscription> get(User user, SubscriptionType subscriptionType, Pageable pageable);
    void updateSubscription(User user, Subscription subscription);
}
