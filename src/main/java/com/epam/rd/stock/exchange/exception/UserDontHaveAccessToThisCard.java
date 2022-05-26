package com.epam.rd.stock.exchange.exception;

public class UserDontHaveAccessToThisCard extends RuntimeException{
    public UserDontHaveAccessToThisCard(String message) {
        super(message);
    }
}
