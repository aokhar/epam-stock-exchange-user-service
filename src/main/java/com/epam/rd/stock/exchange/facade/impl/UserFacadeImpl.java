package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.*;
import com.epam.rd.stock.exchange.dto.enums.ChangeBalanceType;
import com.epam.rd.stock.exchange.exception.AuthenticationException;
import com.epam.rd.stock.exchange.exception.NotEnoughBalanceException;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.mapper.UserValuableInfoMapper;
import com.epam.rd.stock.exchange.model.BalanceUpdateHistory;
import com.epam.rd.stock.exchange.model.Card;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import com.epam.rd.stock.exchange.service.BalanceUpdateHistoryService;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.CardService;
import com.epam.rd.stock.exchange.util.CardValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserValuableInfoMapper userValuableInfoMapper;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final BalanceUpdateHistoryService balanceUpdateHistoryService;

    @Override
    public UserViewDto findById(String id) {
        User user = userService.findById(id);
        return userMapper.toUserViewDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BigDecimal updateBalance(String email, ChangeBalanceDto changeBalanceDto) {
        CardValidationUtil.validateUserCard(changeBalanceDto);
        User user = userService.findByEmail(email);
        BigDecimal balanceUpdate = changeBalanceDto.getSum();
        BalanceUpdateType type = BalanceUpdateType.TOPUP;
        if(changeBalanceDto.getChangeBalanceType().equals(ChangeBalanceType.WITHDRAW)){
            verifyUserBalance(user.getBalance(), balanceUpdate);
            balanceUpdate = balanceUpdate.multiply(BigDecimal.valueOf(-1));
            type = BalanceUpdateType.WITHDRAW;
        }
        userService.updateBalance(user.getId(), balanceUpdate);
        BalanceUpdateHistory newBalanceUpdateHistory = BalanceUpdateHistory.builder()
                        .update(balanceUpdate)
                        .type(type)
                        .userId(user.getId())
                        .card(changeBalanceDto.getCard())
                        .dateTime(LocalDateTime.now())
                        .build();

       balanceUpdateHistoryService.save(newBalanceUpdateHistory);
       return balanceUpdate;
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
        }
        return userMapper.toUserViewDto(user);
    }

    @Override
    public UserViewDto findByEmail(String email) {
        User user = userService.findByEmail(email);
        UserViewDto userViewDto = userMapper.toUserViewDto(user);
        List<UserValuableInfoViewDto> userValuableInfoViewDtoList = user.getValuables()
                .stream().map(userValuableInfoMapper::toUserValuableInfoViewDto).collect(Collectors.toList());
        userViewDto.setValuables(userValuableInfoViewDtoList);
        return userViewDto;
    }

    @Override
    public UserViewDto registration(UserCreateDto userCreateDto) {
        User user = userService.save(userMapper.toUser(userCreateDto));
        return userMapper.toUserViewDto(user);
    }

    private void verifyUserBalance(BigDecimal userBalance, BigDecimal balanceUpdate){
        if(userBalance.compareTo(balanceUpdate) < 0){
            throw new NotEnoughBalanceException("User don't have enough balance to withdraw");
        }
    }


}
