package com.epam.rd.stock.exchange.exception;

public class NotEnoughBalanceException extends RuntimeException{
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
