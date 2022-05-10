package com.epam.rd.stock.exchange.filter;

import com.epam.rd.stock.exchange.exception.AuthenticationException;
import com.epam.rd.stock.exchange.exception.InvalidTokenException;
import com.epam.rd.stock.exchange.exception.UserBlockedException;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(0)
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    public static final String ERROR_ATTRIBUTE = "error";
    public static final String ERROR_LOGIN_PAGE = "/loginError";


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UserNotFoundException | InvalidTokenException | AuthenticationException | UserBlockedException e) {
            setErrorResponse(request, response, e);
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                  Exception ex) throws IOException, ServletException {
        if (ex instanceof UserNotFoundException || ex instanceof AuthenticationException) {
            request.setAttribute(ERROR_ATTRIBUTE, "Invalid email or password");
        } else if (ex instanceof UserBlockedException) {
            request.setAttribute(ERROR_ATTRIBUTE, "Account blocked");
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, ex.getMessage());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(request.getContextPath() + ERROR_LOGIN_PAGE);
        dispatcher.forward(request, response);
    }

}

