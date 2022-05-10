package com.epam.rd.stock.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserViewDto {

    private String id;

    private String firstname;

    private String lastname;

    private String email;

    private List<UserStockInfoViewDto> stocks;

    private boolean isBlocked;
}
