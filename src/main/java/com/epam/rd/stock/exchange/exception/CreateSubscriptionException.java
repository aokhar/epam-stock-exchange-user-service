package com.epam.rd.stock.exchange.exception;

public class CreateSubscriptionException extends RuntimeException{
    public CreateSubscriptionException(String message) {
        super(message);
    }
}