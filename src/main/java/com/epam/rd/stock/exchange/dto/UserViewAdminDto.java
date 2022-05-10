package com.epam.rd.stock.exchange.dto;

import com.epam.rd.stock.exchange.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserViewAdminDto {
    private String id;

    private String firstname;

    private String lastname;

    private String email;

    private boolean isBlocked;

    private UserRole userRole;

    private BigDecimal balance;
}
