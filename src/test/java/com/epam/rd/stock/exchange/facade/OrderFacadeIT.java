package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.repository.OrderRepository;
import com.epam.rd.stock.exchange.repository.StockRepository;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.repository.UserStockInfoRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderFacadeIT extends AbstractIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserStockInfoRepository userStockInfoRepository;

    @Autowired
    private WalletRepository walletRepository;

    private Order order;
    private User user;
    private Stock stock;
    private UserStockInfo userStockInfo;
    private Wallet wallet;
    private Pageable pageable;

    @BeforeEach
    public void init() {
        order = EntityGenerator.generateDomainOrder();
        pageable = EntityGenerator.generatePageable();
        user = EntityGenerator.generateDomainUser();
        stock = EntityGenerator.generateDomainStock();
        userStockInfo = EntityGenerator.generateUserStockInfo();
        wallet = EntityGenerator.generateDomainWallet();
    }

    @Test
    public void shouldReturnOrdersByUserIdAndStatus() {
        // Given
        User repoUser = userRepository.save(user);
        Stock repoStock = stockRepository.save(stock);
        order.setUser(repoUser);
        order.setStock(repoStock);
        Order repoOrder = orderRepository.save(order);

        // When
        Page<OrderViewDto> orderViewDto = orderFacade.findByUserIdAndStatus(repoOrder.getUser().getId(), null, 1, pageable.getPageSize());

        // Then
        assertEquals(orderViewDto.getContent().size(), 1);
    }

    @Test
    public void shouldCancelOrder() {
        // Given
        User repoUser = userRepository.save(user);
        Stock repoStock = stockRepository.save(stock);
        order.setUser(repoUser);
        order.setStock(repoStock);
        order.setStatus(OrderStatus.ACTIVE);
        Order repoOrder = orderRepository.save(order);

        // When
        orderFacade.cancel(repoOrder.getId());

        //Then
        assertEquals(orderRepository.findById(repoOrder.getId()).get().getStatus(), OrderStatus.CANCELED);
    }

    @Test
    public void shouldSubmitOrder() {
        // Given
        User repoUser = userRepository.save(user);
        Stock repoStock = stockRepository.save(stock);
        order.setUser(repoUser);
        order.setStock(repoStock);

        // When
        String id = orderFacade.submit(toOrderCreateDto(order));

        //Then
        assertNotNull(orderRepository.findById(id).get());
    }

    @Test
    public void shouldProcessOrder() {
        // Given
        User repoUser = userRepository.save(user);
        Stock repoStock = stockRepository.save(stock);

        wallet.setUserId(repoUser.getId());
        wallet.setBalance(repoStock.getPrice().multiply(BigDecimal.valueOf(1)));
        Wallet repoWallet = walletRepository.save(wallet);

        userStockInfo.setUser(repoUser);
        userStockInfo.setStock(repoStock);
        Integer oldAmount = userStockInfo.getAmount();
        UserStockInfo repoUserStockInfo = userStockInfoRepository.save(userStockInfo);

        order.setUser(repoUser);
        order.setStock(repoStock);
        order.setExpectedStockPrice(repoStock.getPrice());
        order.setAmount(1);
        Order repoOrder = orderRepository.save(order);

        //When
        OrderViewDto orderViewDto = orderFacade.process(repoOrder.getId());

        //Then
        assertNotEquals(OrderStatus.ACTIVE, orderViewDto.getStatus());
        assertEquals(0, repoWallet.getBalance().intValue());
        assertEquals(oldAmount + orderViewDto.getAmount(), repoUserStockInfo.getAmount());
    }

    private OrderCreateDto toOrderCreateDto(Order order) {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setStockId(order.getStock().getId());
        orderCreateDto.setExpectedStockPrice(order.getExpectedStockPrice());
        orderCreateDto.setType(order.getType());
        orderCreateDto.setUserEmail(order.getUser().getEmail());
        orderCreateDto.setAmount(order.getAmount());
        return orderCreateDto;
    }
}
