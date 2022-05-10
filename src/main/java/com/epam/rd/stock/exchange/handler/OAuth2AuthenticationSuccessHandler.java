package com.epam.rd.stock.exchange.handler;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserMapper userMapper;

    private final UserFacade userFacade;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        UserCreateDto userCreateDto = userMapper.getUserCreateDto(user);

        userFacade.signInWithSocialNetwork(userCreateDto);
        String userId = userFacade.findByEmail(userCreateDto.getEmail()).getId();
        jwtTokenUtil.createAndAddTokenIntoSession(userId, request);
        response.sendRedirect("/profile");
    }

}
