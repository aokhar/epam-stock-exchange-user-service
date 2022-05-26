package com.epam.rd.stock.exchange.exception;

public class SubscriptionDoesntExistException extends RuntimeException {
    public SubscriptionDoesntExistException(String message) {
        super(message);
    }
}
