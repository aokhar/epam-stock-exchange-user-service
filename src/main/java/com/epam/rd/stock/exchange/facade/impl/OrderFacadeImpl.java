package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.exception.ProcessOrderException;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.mapper.OrderMapper;
import com.epam.rd.stock.exchange.mapper.UserStockInfoMapper;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.enums.OrderType;
import com.epam.rd.stock.exchange.service.OrderService;
import com.epam.rd.stock.exchange.service.StockService;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.UserStockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService orderService;
    private final StockService stockService;
    private final StockFacade stockFacade;
    private final UserService userService;
    private final UserStockInfoService userStockInfoService;
    private final OrderMapper orderMapper;
    private final UserStockInfoMapper userStockInfoMapper;

    @Override
    public String submit(OrderCreateDto orderCreateDto) {
        LocalDateTime timeSubmitted = LocalDateTime.now();

        Stock stock = stockService.findById(orderCreateDto.getStockId());
        User user = userService.findByEmail(orderCreateDto.getUserEmail());

        Order order = orderMapper.toOrder(stock, user, orderCreateDto);

        order.setExpectedOrderPrice(calculateExpectedOrderPrice(orderCreateDto));
        order.setTimeSubmitted(timeSubmitted);
        order.setStatus(OrderStatus.ACTIVE);

        return orderService.save(order).getId();
    }

    @Override
    public OrderViewDto process(String orderId) {
        Order order = orderService.findById(orderId);
        return orderMapper.toOrderDto(processOrder(order));
    }

    @Override
    public void cancel(String orderId) {
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.CANCELED);
        orderService.save(order);
    }

    @Override
    public Page<OrderViewDto> findByUserIdAndStatus(String userId, OrderStatus status, Integer page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (status == null) {
            return orderMapper.toPageOrderDto(orderService.findByUserId(userId, pageable));
        }
        return orderMapper.toPageOrderDto(orderService.findByUserIdAndStatus(userId, status, pageable));
    }

    @Override
    public void processAll() {
        List<UserStockInfo> userStockInfoList = userStockInfoService.findAll();
        userStockInfoList.stream().forEach(this::processUserStockInfo);
        List<Order> orderList = orderService.findByStatus(OrderStatus.ACTIVE);
        orderList.stream().forEach(this::processOrder);
    }

    private Order processOrder(Order order) {
        if (canBeProcessed(order)) {
            order.setActualOrderPrice(calculateActualOrderPrice(order));
            try {
                switch (order.getType()) {
                    case BUY:
                        stockFacade.buy(order);
                        break;
                    case SELL:
                        stockFacade.sell(order);
                        break;
                }
                order.setStatus(OrderStatus.SUCCESS);
            } catch (ProcessOrderException e) {
                order.setStatus(OrderStatus.FAIL);
                order.setFailDescription(e.getMessage());
            }
            LocalDateTime timeProcessed = LocalDateTime.now();
            order.setTimeProcessed(timeProcessed);
        }
        return orderService.save(order);
    }

    private boolean canBeProcessed(Order order) {
        if (order.getType() == OrderType.BUY) {
            return order.getStock().getPrice().doubleValue() <= order.getExpectedStockPrice().doubleValue();
        } else {
            return order.getStock().getPrice().doubleValue() >= order.getExpectedStockPrice().doubleValue();
        }
    }

    private BigDecimal calculateExpectedOrderPrice(OrderCreateDto orderCreateDto) {
        return orderCreateDto.getExpectedStockPrice().multiply(BigDecimal.valueOf(orderCreateDto.getAmount())).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateActualOrderPrice(Order order) {
        return order.getStock().getPrice().multiply(BigDecimal.valueOf(order.getAmount())).setScale(2, RoundingMode.HALF_UP);
    }

    private void processUserStockInfo(UserStockInfo userStockInfo) {
        if (canBeProcessed(userStockInfo)) {
            OrderCreateDto orderCreateDto = userStockInfoMapper.toOrderCreateDto(userStockInfo);
            orderCreateDto.setType(OrderType.SELL);
            submit(orderCreateDto);
            resetTakeProfitStopLoss(userStockInfo);
        }
    }

    private void resetTakeProfitStopLoss(UserStockInfo userStockInfo) {
        userStockInfo.setTakeProfit(null);
        userStockInfo.setStopLoss(null);
        userStockInfo.setAmountToSell(null);
        userStockInfoService.save(userStockInfo);
    }

    private boolean canBeProcessed(UserStockInfo userStockInfo) {
        double stockPrice = userStockInfo.getStock().getPrice().doubleValue();
        boolean isStopLoss = userStockInfo.getStopLoss() != null && stockPrice <= userStockInfo.getStopLoss().doubleValue();
        boolean isTakeProfit = userStockInfo.getTakeProfit() != null && stockPrice >= userStockInfo.getTakeProfit().doubleValue();
        return isStopLoss || isTakeProfit;
    }

}
