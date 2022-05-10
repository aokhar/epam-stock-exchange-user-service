package com.epam.rd.stock.exchange.datagenerator;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.Stock;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.model.Wallet;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.enums.OrderType;
import com.epam.rd.stock.exchange.model.enums.StockType;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class EntityGenerator {

    private final Random random = new Random();

    public static User generateDomainUser() {
        String emailPart = StringGenerator.generateRandomString(10);
        return User.builder()
                .id(UUID.randomUUID().toString())
                .firstName(StringGenerator.generateRandomString(5))
                .lastName(StringGenerator.generateRandomString(5))
                .role(UserRole.USER)
                .email(emailPart + "@gmail.com")
                .actualEmail(emailPart + "@gmail.com")
                .password(StringGenerator.generateRandomString(10))
                .isBlocked(false)
                .build();
    }

    public static Wallet generateDomainWallet() {
        return Wallet.builder()
                .id(UUID.randomUUID().toString())
                .balance(BigDecimalGenerator.generateRandomDecimal())
                .userId(UUID.randomUUID().toString())
                .cardHolder(StringGenerator.generateRandomString(14))
                .expMonth(random.nextInt())
                .expYear(random.nextInt())
                .card(random.nextLong())
                .cvc(random.nextInt())
                .build();
    }

    public static Stock generateDomainStock() {
        return Stock.builder()
                .id(UUID.randomUUID().toString())
                .symbol(StringGenerator.generateRandomString(4))
                .name(StringGenerator.generateRandomString(16))
                .price(BigDecimalGenerator.generateRandomDecimal())
                .trend(BigDecimalGenerator.generateRandomDecimal())
                .type(StockType.STOCK)
                .build();
    }

    public static Pageable generatePageable() {
        Pageable pageable = PageRequest.of(0, 3);
        return pageable;
    }

    public static Order generateDomainOrder() {
        return Order.builder()
                .id(UUID.randomUUID().toString())
                .amount(random.nextInt())
                .status(OrderStatus.ACTIVE)
                .expectedOrderPrice(BigDecimalGenerator.generateRandomDecimal())
                .expectedStockPrice(BigDecimalGenerator.generateRandomDecimal())
                .type(OrderType.BUY)
                .timeSubmitted(LocalDateTime.now())
                .timeProcessed(LocalDateTime.now())
                .failDescription(StringGenerator.generateRandomString(20))
                .build();
    }

    public static UserStockInfo generateUserStockInfo() {
        return UserStockInfo.builder()
                .id(UUID.randomUUID().toString())
                .amount(random.nextInt())
                .build();
    }

    public static UserCreateDto generateUserCreateDto() {
        return UserCreateDto.builder()
                .firstname(StringGenerator.generateRandomString(5))
                .lastname(StringGenerator.generateRandomString(5))
                .email(StringGenerator.generateRandomString(10) + "@gmail.com")
                .password(StringGenerator.generateRandomString(10))
                .build();
    }
}
