package com.epam.rd.stock.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeWalletBalanceDto {

    private String id;

    private String userId;

    @NotNull(message = "Balance must not be empty!")
    private BigDecimal balance;

    @NotNull(message = "Balance must not be empty!")
    @Range(min = 1, message = "Please select positive numbers Only")
    private BigDecimal sum;

    @NotEmpty(message = "CardHolder must not be blank!")
    @Size(min = 5, max = 30, message = "CardHolder must be at least 5 symbols")
    private String cardHolder;

    @NotNull(message = "ExpMonth must not be empty!")
    @Digits(message = "Invalid value", integer = 2, fraction = 0)
    private Integer expMonth;

    @NotNull(message = "ExpYear must not be empty!")
    @Digits(message = "Invalid value", integer = 4, fraction = 0)
    private Integer expYear;

    @NotNull(message = "Card must not be empty!")
    @Digits(message = "Invalid value", integer = 16, fraction = 0)
    private Long card;

    @NotNull(message = "Cvc must not be empty!")
    @Digits(message = "Invalid value", integer = 3, fraction = 0)
    private Integer cvc;
}
