package com.epam.rd.stock.exchange.exception;

public class CardNotValidException extends RuntimeException{
    public CardNotValidException(String message) {
        super(message);
    }
}
