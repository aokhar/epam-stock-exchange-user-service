package com.epam.rd.stock.exchange.exception;

public class ProcessOrderException extends RuntimeException {
    public ProcessOrderException(String message) {
        super(message);
    }
}
