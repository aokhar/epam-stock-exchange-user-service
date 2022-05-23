package com.epam.rd.stock.exchange.exception;

public class ValuableNotFoundException extends RuntimeException{
    public ValuableNotFoundException(String message) {
        super(message);
    }
}
