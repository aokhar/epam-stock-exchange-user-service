package com.epam.rd.stock.exchange.exception;

public class UserBlockedException extends RuntimeException{
    public UserBlockedException(String message) {
        super(message);
    }
}
