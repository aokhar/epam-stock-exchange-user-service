package com.epam.rd.stock.exchange.datagenerator;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class StringGenerator {
    public static String generateRandomString(int length) {
        if (length <= 0) {
            length = 10;
        }
        int leftLimit = 48;
        int rightLimit = 122;

        return new Random().ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
