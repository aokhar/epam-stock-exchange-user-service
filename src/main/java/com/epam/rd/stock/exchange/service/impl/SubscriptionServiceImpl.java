package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.SubscriptionDoesntExistException;
import com.epam.rd.stock.exchange.exception.UserDontHaveAccessToThisSubscriptionException;
import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import com.epam.rd.stock.exchange.repository.SubscriptionRepository;
import com.epam.rd.stock.exchange.service.SubscriptionService;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(User user, String subscriptionId) {
       Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
       if(optionalSubscription.isEmpty()){
           throw new SubscriptionDoesntExistException("Subscription doesn't exist");
       }
       Subscription subscription = optionalSubscription.get();
       if(!user.getId().equals(subscription.getUserId())){
           throw new UserDontHaveAccessToThisSubscriptionException("This is not current user subscription");
       }
       subscriptionRepository.delete(subscription);
    }

    @Override
    public Subscription get(User user, String subscriptionId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if(optionalSubscription.isEmpty()){
            throw new SubscriptionDoesntExistException("Subscription doesn't exist");
        }
        Subscription subscription = optionalSubscription.get();
        if(!user.getId().equals(subscription.getUserId())){
            throw new UserDontHaveAccessToThisSubscriptionException("This is not current user subscription");
        }
        return subscription;
    }

    @Override
    public Page<Subscription> get(User user, Pageable pageable) {
        return subscriptionRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    public Page<Subscription> get(User user, SubscriptionType subscriptionType, Pageable pageable) {
        return subscriptionRepository.findByUserIdAndType(user.getId(), subscriptionType, pageable);
    }

    @Override
    public void updateSubscription(User user, Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscription.getId());
        if(optionalSubscription.isEmpty()){
            throw new SubscriptionDoesntExistException("Subscription doesn't exist");
        }
        Subscription oldSubscription = optionalSubscription.get();
        if(!user.getId().equals(oldSubscription.getUserId())){
            throw new UserDontHaveAccessToThisSubscriptionException("This is not current user subscription");
        }
        subscriptionRepository.save(subscription);
    }
}
