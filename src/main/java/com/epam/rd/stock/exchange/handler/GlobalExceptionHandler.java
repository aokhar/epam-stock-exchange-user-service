package com.epam.rd.stock.exchange.handler;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.exception.AuthenticationException;
import com.epam.rd.stock.exchange.exception.EntityAlreadyExistsException;
import com.epam.rd.stock.exchange.exception.OrderNotFoundException;
import com.epam.rd.stock.exchange.exception.ProcessOrderException;
import com.epam.rd.stock.exchange.exception.ValuableNotFoundException;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public final String handleEntityAlreadyExistsException(HttpServletRequest request,
                                                           RedirectAttributes redirectAttributes,
                                                           Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        redirectAttributes.addFlashAttribute("user", new UserCreateDto());
        log.error(ex.getMessage());
        return "redirect:" + request.getRequestURI();
    }

    @ExceptionHandler({UserNotFoundException.class, AuthenticationException.class})
    public final String handleLoginExceptions(HttpServletRequest req,
                                              RedirectAttributes redirectAttributes,
                                              Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        log.error(ex.getMessage());
        return "redirect:" + req.getRequestURI();
    }

    @ExceptionHandler({ProcessOrderException.class,
            OrderNotFoundException.class})
    public final String handleOrderExceptions(HttpServletRequest req,
                                              RedirectAttributes redirectAttributes,
                                              Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        log.error(ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler({ValuableNotFoundException.class})
    public final String handleStockExceptions(HttpServletRequest req,
                                              RedirectAttributes redirectAttributes,
                                              Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        log.error(ex.getMessage());
        return "redirect:/";
    }
}
