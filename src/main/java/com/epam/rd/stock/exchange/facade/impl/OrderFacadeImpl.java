package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.exception.ProcessOrderException;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.ValuableFacade;
import com.epam.rd.stock.exchange.mapper.OrderMapper;
import com.epam.rd.stock.exchange.mapper.UserValuableInfoMapper;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.enums.ValuableType;
import com.epam.rd.stock.exchange.service.OrderService;
import com.epam.rd.stock.exchange.service.ValuableService;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.UserValuableInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService orderService;
    private final ValuableService valuableService;
    private final ValuableFacade valuableFacade;
    private final UserService userService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OrderViewDto submit(OrderCreateDto orderCreateDto) {
        LocalDateTime timeSubmitted = LocalDateTime.now();

        Valuable valuable = valuableService.findById(orderCreateDto.getValuableId());
        User user = userService.findByEmail(orderCreateDto.getUserEmail());

        if(!valuable.getType().equals(ValuableType.CRYPTO) && !isIntegerValue(orderCreateDto.getAmount())){
            throw new ProcessOrderException("This valuable are not available to operate in not round amounts!");
        }

        Order order = orderMapper.toOrder(valuable, user, orderCreateDto);

        order.setValuablePrice(valuable.getPrice());
        order.setDateTime(timeSubmitted);

        order.setOrderPrice(calculateOrderPrice(valuable.getPrice(), order.getAmount()));
        try {
            switch (order.getType()) {
                case BUY:
                    valuableFacade.buy(order);
                    break;
                case SELL:
                    valuableFacade.sell(order);
                    break;
            }
            order.setStatus(OrderStatus.SUCCESS);
        } catch (ProcessOrderException e) {
            order.setStatus(OrderStatus.FAIL);
            order.setFailDescription(e.getMessage());
        }
        LocalDateTime timeProcessed = LocalDateTime.now();
        order.setDateTime(timeProcessed);

        Order newOrder = orderService.save(order);
        return orderMapper.toOrderDto(newOrder);
    }

    @Override
    public Page<OrderViewDto> findByUserIdAndStatus(String userId, OrderStatus status, Integer page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (status == null) {
            return orderMapper.toPageOrderDto(orderService.findByUserId(userId, pageable));
        }
        return orderMapper.toPageOrderDto(orderService.findByUserIdAndStatus(userId, status, pageable));
    }


    private BigDecimal calculateOrderPrice(BigDecimal valuablePrice, BigDecimal amount) {
        return valuablePrice.multiply(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isIntegerValue(BigDecimal bd) {
        boolean ret;
        try {
            bd.toBigIntegerExact();
            ret = true;
        } catch (ArithmeticException ex) {
            ret = false;
        }
        return ret;
    }

}
