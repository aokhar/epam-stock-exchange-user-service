package com.epam.rd.stock.exchange.exception;

public class CardExistException extends RuntimeException {

    public CardExistException(String message) {
        super(message);
    }

}
