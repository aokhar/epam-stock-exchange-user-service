package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.CreateSubscriptionDto;
import com.epam.rd.stock.exchange.dto.SubscriptionViewDto;
import com.epam.rd.stock.exchange.dto.UpdateSubscriptionDto;
import com.epam.rd.stock.exchange.exception.CreateSubscriptionException;
import com.epam.rd.stock.exchange.exception.UserDontHaveEnoughValuablesForSubscriptionException;
import com.epam.rd.stock.exchange.facade.SubscriptionFacade;
import com.epam.rd.stock.exchange.mapper.SubscriptionMapper;
import com.epam.rd.stock.exchange.model.*;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.enums.OrderType;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import com.epam.rd.stock.exchange.model.enums.ValuableType;
import com.epam.rd.stock.exchange.model.util.ConditionVerifyUtil;
import com.epam.rd.stock.exchange.service.OrderService;
import com.epam.rd.stock.exchange.service.SubscriptionService;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.ValuableService;
import com.epam.rd.stock.exchange.service.impl.UserValuableInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionFacadeImpl implements SubscriptionFacade {

    private final SubscriptionService subscriptionService;

    private final UserService userService;

    private final ValuableService valuableService;

    private final UserValuableInfoServiceImpl userValuableInfoService;

    private final SubscriptionMapper subscriptionMapper;

    private OrderService orderService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public SubscriptionViewDto createSubscription(String email, CreateSubscriptionDto createSubscription) {
        User user = userService.findByEmail(email);
        Valuable valuable = valuableService.findById(createSubscription.getValuableId());
        if(!valuable.getType().equals(ValuableType.CRYPTO) && !createSubscription.getSubscriptionType().equals(SubscriptionType.INFORM) && !isIntegerValue(createSubscription.getAmount())){
            throw new CreateSubscriptionException("This valuable are not available to operate in not round amounts!");
        }

        Subscription subscription = subscriptionMapper.toSubscription(createSubscription, user, valuable);
        UserValuableInfo userValuableInfo = userValuableInfoService.findUserStockInfo(user.getId(), valuable.getId());
        if(userValuableInfo == null && subscription.isReserve() && subscription.getType().equals(SubscriptionType.SELL) ){
            throw new UserDontHaveEnoughValuablesForSubscriptionException("User don't have enough valuables to reserve");
        }
        else if( userValuableInfo != null && subscription.isReserve() && subscription.getType().equals(SubscriptionType.SELL)&&
                userValuableInfo.getSellAmount().add(subscription.getAmount()).compareTo(userValuableInfo.getAmount())>0){
            throw new UserDontHaveEnoughValuablesForSubscriptionException("User don't have enough valuables to reserve");
        }
        else if( userValuableInfo != null && subscription.isReserve() && subscription.getType().equals(SubscriptionType.SELL)&&
                userValuableInfo.getSellAmount().add(subscription.getAmount()).compareTo(userValuableInfo.getAmount())<=0){
            userValuableInfo.setSellAmount(userValuableInfo.getSellAmount().add(subscription.getAmount()));
            userValuableInfoService.save(userValuableInfo);
        }
        boolean process = ConditionVerifyUtil.verify(subscription, valuable.getPrice());
        return subscriptionMapper.toUserView(subscriptionService.createSubscription(subscription));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteSubscription(String email, String subscriptionId) {
        User user = userService.findByEmail(email);
        Subscription  subscription = subscriptionService.get(user,subscriptionId);
        UserValuableInfo userValuableInfo = userValuableInfoService.findUserStockInfo(user.getId(), subscription.getValuableId());
        if(subscription.getType().equals(SubscriptionType.SELL) && subscription.isReserve()){
            userValuableInfo.setSellAmount(userValuableInfo.getSellAmount().subtract(subscription.getAmount()));
            userValuableInfoService.save(userValuableInfo);
        }
        subscriptionService.deleteSubscription(user, subscriptionId);
    }


    @Override
    public Page<SubscriptionViewDto> get(String email, int page, int pageSize) {
        User user = userService.findByEmail(email);
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return subscriptionService.get(user,pageable).map(subscriptionMapper::toUserView);
    }

    @Override
    public Page<SubscriptionViewDto> get(String email, SubscriptionType subscriptionType, int page, int pageSize) {
        User user = userService.findByEmail(email);
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return subscriptionService.get(user, subscriptionType, pageable).map(subscriptionMapper::toUserView);
    }

    private boolean isIntegerValue(BigDecimal price){
        boolean ret;
        try {
            price.toBigIntegerExact();
            ret = true;
        } catch (ArithmeticException ex) {
            ret = false;
        }
        return ret;
    }
}
