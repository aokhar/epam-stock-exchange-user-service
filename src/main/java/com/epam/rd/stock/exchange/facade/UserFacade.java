package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.dto.UserSignInDto;
import com.epam.rd.stock.exchange.dto.UserViewAdminDto;
import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import org.springframework.data.domain.Page;

public interface UserFacade {

    UserViewDto registration(UserCreateDto userCreateDto);

    UserViewDto signIn(UserSignInDto userSignInDto);

    UserViewDto signInWithSocialNetwork(UserCreateDto userCreateDto);

    UserViewDto findByEmail(String email);

    UserViewDto findById(String id);

    void updateBlockingStatus(String email, boolean isBlocked);

    Page<UserViewAdminDto> findAllUsersByEmail(String email, int page, int size);

    void updateRole(String email, UserRole userRole);
}
