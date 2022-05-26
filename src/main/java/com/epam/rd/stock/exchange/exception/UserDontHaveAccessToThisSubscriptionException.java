package com.epam.rd.stock.exchange.exception;

public class UserDontHaveAccessToThisSubscriptionException extends RuntimeException {
    public UserDontHaveAccessToThisSubscriptionException(String message) {
        super(message);
    }
}
