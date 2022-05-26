package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    Page<Subscription> findByUserId(String userId, Pageable pageable);

    Page<Subscription> findByUserIdAndType(String userId, SubscriptionType type, Pageable pageable);
}
