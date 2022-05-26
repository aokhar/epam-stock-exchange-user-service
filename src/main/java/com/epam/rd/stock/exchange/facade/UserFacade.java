package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.*;
import com.epam.rd.stock.exchange.dto.enums.ChangeBalanceType;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface UserFacade {

    UserViewDto registration(UserCreateDto userCreateDto);

    UserViewDto signIn(UserSignInDto userSignInDto);

    UserViewDto signInWithSocialNetwork(UserCreateDto userCreateDto);

    UserViewDto findByEmail(String email);

    UserViewDto findById(String id);

    BigDecimal updateBalance(String email, ChangeBalanceDto changeBalanceDto);
}
