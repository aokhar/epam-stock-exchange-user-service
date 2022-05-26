package com.epam.rd.stock.exchange.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class UserSignInDto {

    @Email
    private String email;

    private String password;

}
