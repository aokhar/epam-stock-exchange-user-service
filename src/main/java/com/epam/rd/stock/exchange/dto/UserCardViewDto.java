package com.epam.rd.stock.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardViewDto {

    private String id;

    private String cardHolder;

    private Integer expMonth;

    private Integer expYear;

    private Long card;

    private Integer cvc;

}
