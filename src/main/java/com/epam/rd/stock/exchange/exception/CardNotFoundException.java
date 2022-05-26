package com.epam.rd.stock.exchange.exception;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String message) {
        super(message);
    }

}
