package com.epam.rd.stock.exchange.filter;

import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.exception.InvalidTokenException;
import com.epam.rd.stock.exchange.exception.UserBlockedException;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.facade.WalletFacade;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.service.AuthenticationService;
import com.epam.rd.stock.exchange.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class TokenFilter extends OncePerRequestFilter {

    private static final String LOGIN_ENDPOINT = "/login";

    private static final String CSRF_TOKEN_ATTRIBUTE = "_csrf";

    @Value("${security.anonymousEndPoints}")
    private final String[] anonymousEndPoints;

    @Value("${security.freeEndPoints}")
    private final String[] freeEndPoints;

    private final UserFacade userFacade;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    private final JwtTokenUtil jwtTokenUtil;

    private final WalletFacade walletFacade;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        String method = request.getMethod();
        if (HttpMethod.POST.matches(method) && path.equals(LOGIN_ENDPOINT)) {
            doLoginPost(request);
        } else if (!Arrays.asList(freeEndPoints).contains(path) && !Arrays.asList(anonymousEndPoints).contains(path)) {
            doOnNotFreeEndpoints(request);
        }
        addSessionParamsForWallet(request);
        filterChain.doFilter(request, response);
    }

    private void doLoginPost(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        userFacade.signIn(userMapper.toUserSignInDto(email, password));
        String userId = userFacade.findByEmail(email).getId();
        UserViewDto user = userFacade.findById(userId);
        checkForBlocking(user);

        jwtTokenUtil.createAndAddTokenIntoSession(userId, request);
        authenticationService.authenticateUser(userMapper.toUserSignInDto(email, password));
    }

    private void doOnNotFreeEndpoints(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CSRF_TOKEN_ATTRIBUTE);
        String userId = jwtTokenUtil.getUserIdFromToken(csrfToken.getToken(), request);
        if (userId != null) {
            UserViewDto user = userFacade.findById(userId);
            checkForBlocking(user);
            authenticationService.authenticateUser(userMapper.
                    toUserSignInDto(userFacade.findById(userId).getEmail(), ""));
        }
    }

    private void addSessionParamsForWallet(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CSRF_TOKEN_ATTRIBUTE);
        try {
            String userId = jwtTokenUtil.getUserIdFromToken(csrfToken.getToken(), request);
            String email = userFacade.findById(userId).getEmail();
            BigDecimal balance = walletFacade.findByUserId(userId).getBalance();
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("balance", balance);
        } catch (InvalidTokenException e) {
            log.info("User is not authorized");
        }
    }

    private void checkForBlocking(UserViewDto user) {
        if (user.isBlocked()) {
            authenticationService.removeUserFromSecurityContext();
            throw new UserBlockedException("Your account was blocked");
        }
    }
}
