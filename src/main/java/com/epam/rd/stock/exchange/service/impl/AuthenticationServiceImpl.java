package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.dto.UserSignInDto;
import com.epam.rd.stock.exchange.service.AuthenticationService;
import com.epam.rd.stock.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    @Override
    public void authenticateUser(UserSignInDto user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userService.getUserRole(user.getEmail()).toString()));
        String password = user.getPassword() != null ? user.getPassword() : "";

        UserDetails userDetails = new User(user.getEmail(), password, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User with email " + user.getEmail() + " has authenticated");
    }

    @Override
    public void removeUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            authentication.setAuthenticated(false);
        }
    }
}
