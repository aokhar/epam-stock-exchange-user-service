package com.epam.rd.stock.exchange.exception;

public class AlertDoesntExistException extends RuntimeException {

    public AlertDoesntExistException(String message) {
        super(message);
    }

}
