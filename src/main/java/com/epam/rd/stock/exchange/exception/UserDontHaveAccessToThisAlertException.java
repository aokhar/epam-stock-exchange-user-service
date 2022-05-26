package com.epam.rd.stock.exchange.exception;

public class UserDontHaveAccessToThisAlertException extends RuntimeException{
    public UserDontHaveAccessToThisAlertException(String message) {
        super(message);
    }
}
