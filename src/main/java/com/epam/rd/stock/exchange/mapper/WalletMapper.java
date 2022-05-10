package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class WalletMapper {

    public ChangeWalletBalanceDto toViewDto(Wallet wallet) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0D).setScale(2, RoundingMode.HALF_UP);
        return ChangeWalletBalanceDto.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .sum(bigDecimal)
                .userId(wallet.getUserId())
                .cardHolder(wallet.getCardHolder())
                .expMonth(wallet.getExpMonth())
                .expYear(wallet.getExpYear())
                .card(wallet.getCard())
                .cvc(wallet.getCvc())
                .build();
    }

}
