package com.epam.rd.stock.exchange.exception;

public class UserStockInfoNotFoundException extends RuntimeException {
    public UserStockInfoNotFoundException(String message) {
        super(message);
    }
}
