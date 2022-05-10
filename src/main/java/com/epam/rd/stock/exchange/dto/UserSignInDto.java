package com.epam.rd.stock.exchange.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInDto {

    private String email;

    private String password;

}
