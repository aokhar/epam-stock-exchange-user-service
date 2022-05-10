package com.epam.rd.stock.exchange.datagenerator;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@UtilityClass
public class BigDecimalGenerator {

    private final Random random = new Random();

    public BigDecimal generateRandomDecimal() {
        return BigDecimal.valueOf(random.nextDouble()).setScale(2, RoundingMode.HALF_UP);
    }
}
