package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.dto.UserSignInDto;

public interface AuthenticationService {

    void authenticateUser(UserSignInDto user);

    void removeUserFromSecurityContext();

}
