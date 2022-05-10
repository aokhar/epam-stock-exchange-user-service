package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.dto.UserSignInDto;
import com.epam.rd.stock.exchange.dto.UserStockInfoViewDto;
import com.epam.rd.stock.exchange.dto.UserViewAdminDto;
import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.exception.AuthenticationException;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.mapper.UserStockInfoMapper;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.Wallet;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserStockInfoMapper userStockInfoMapper;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final WalletService walletService;

    @Value("${registration.bonus}")
    private BigDecimal registrationBonus;

    @Override
    public UserViewDto findById(String id) {
        User user = userService.findById(id);
        return userMapper.toUserViewDto(user);
    }

    @Override
    public void updateBlockingStatus(String email, boolean isBlocked) {
        userService.updateBlockingStatus(email, isBlocked);
    }

    @Override
    public Page<UserViewAdminDto> findAllUsersByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserViewAdminDto> list = userMapper.toUserViewAdminDtoPage(userService.findAllUsersByEmail(email, pageable));
        return updateUsersBalances(list, pageable);
    }

    @Override
    public void updateRole(String email, UserRole userRole) {
        userService.updateRole(email, userRole);
    }

    @Override
    public UserViewDto signIn(UserSignInDto userSignInDto) {
        User user = userService.findByEmail(userSignInDto.getEmail());
        if (!passwordEncoder.matches(userSignInDto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid login or password");
        }
        return userMapper.toUserViewDto(user);
    }

    @Override
    public UserViewDto signInWithSocialNetwork(UserCreateDto userCreateDto) {
        User user;
        try {
            user = userService.findByEmail(userCreateDto.getEmail());
        } catch (UserNotFoundException e) {
            user = userService.save(userMapper.toUserSocial(userCreateDto));
            walletService.save(createWalletForNewUser(user.getId()));
        }
        return userMapper.toUserViewDto(user);
    }

    @Override
    public UserViewDto findByEmail(String email) {
        User user = userService.findByEmail(email);
        UserViewDto userViewDto = userMapper.toUserViewDto(user);
        List<UserStockInfoViewDto> userStockInfoViewDtoList = user.getStocks()
                .stream().map(userStockInfoMapper::toUserStockInfoViewDto).collect(Collectors.toList());
        userViewDto.setStocks(userStockInfoViewDtoList);
        return userViewDto;
    }

    @Override
    public UserViewDto registration(UserCreateDto userCreateDto) {
        User user = userService.save(userMapper.toUser(userCreateDto));
        walletService.save(createWalletForNewUser(user.getId()));
        return userMapper.toUserViewDto(user);
    }

    private Wallet createWalletForNewUser(String userId) {
        Wallet wallet = new Wallet();
        wallet.setBalance(registrationBonus);
        wallet.setUserId(userId);
        return wallet;
    }

    private Page<UserViewAdminDto> updateUsersBalances(Page<UserViewAdminDto> users, Pageable pageable) {
        List<UserViewAdminDto> usersList = users.getContent();
        for (int i = 0; i < usersList.size(); i++) {
            String userId = usersList.get(i).getId();
            usersList.get(i).setBalance(walletService.findByUserId(userId).getBalance());
        }
        return new PageImpl<>(usersList, pageable, users.getTotalElements());
    }
}
