package com.epam.rd.stock.exchange.exception;

public class UserDontHaveEnoughValuablesForSubscriptionException extends RuntimeException {
    public UserDontHaveEnoughValuablesForSubscriptionException(String message) {
        super(message);
    }
}
