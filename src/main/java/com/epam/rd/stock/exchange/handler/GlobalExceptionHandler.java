package com.epam.rd.stock.exchange.handler;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    @ExceptionHandler({CardExistException.class, CardNotValidException.class,
            UserDontHaveAccessToThisCard.class, NotEnoughBalanceException.class, CardNotFoundException.class})
    public final String handleCardExceptions(HttpServletRequest req,
                                              RedirectAttributes redirectAttributes,
                                              Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        log.error(ex.getMessage());
        return "redirect:/cards";
    }

    @ExceptionHandler({SubscriptionDoesntExistException.class, CreateSubscriptionException.class,
            UserDontHaveAccessToThisSubscriptionException.class, UserDontHaveEnoughValuablesForSubscriptionException.class})
    public final String handleSubscriptionExceptions(HttpServletRequest req,
                                             RedirectAttributes redirectAttributes,
                                             Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        log.error(ex.getMessage());
        return "redirect:/subscriptions";
    }


    @ExceptionHandler({AlertDoesntExistException.class, UserDontHaveAccessToThisAlertException.class})
    public final String handleAlertExceptions(HttpServletRequest req,
                                                     RedirectAttributes redirectAttributes,
                                                     Exception ex) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        log.error(ex.getMessage());
        return "redirect:/alerts";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final String handleMissingRequestParameterException(HttpServletRequest req,
                                              RedirectAttributes redirectAttributes,
                                              Exception ex) {
        redirectAttributes.addFlashAttribute("error", "Something went wrong!");
        log.error(ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public final String handleRuntimeExceptions(HttpServletRequest req,
                                                       RedirectAttributes redirectAttributes,
                                                       Exception ex) {
        redirectAttributes.addFlashAttribute("error", "Something went wrong!");
        log.error(ex.getMessage());
        return "redirect:/";
    }

}
