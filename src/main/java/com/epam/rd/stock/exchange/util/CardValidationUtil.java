package com.epam.rd.stock.exchange.util;

import com.epam.rd.stock.exchange.dto.ChangeBalanceDto;
import com.epam.rd.stock.exchange.dto.NewCardDto;
import com.epam.rd.stock.exchange.dto.UpdateCardDto;
import com.epam.rd.stock.exchange.exception.CardNotValidException;

import java.time.LocalDateTime;

public class CardValidationUtil {

    public static void validateUserCard(NewCardDto newCardDto){
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthNumber = localDateTime.getMonthValue();
        int yearNumber = localDateTime.getYear();
        if((yearNumber > newCardDto.getExpYear())){
            throw new CardNotValidException("Card is expired!");
        }
        else if((yearNumber == newCardDto.getExpYear() && monthNumber > newCardDto.getExpMonth())){
            throw new CardNotValidException("Card is expired!");
        }
    }

    public static void validateUserCard(UpdateCardDto updateCardDto){
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthNumber = localDateTime.getMonthValue();
        int yearNumber = localDateTime.getYear();
        if((yearNumber > updateCardDto.getExpYear())){
            throw new CardNotValidException("Card is expired!");
        }
        else if((yearNumber == updateCardDto.getExpYear() && monthNumber > updateCardDto.getExpMonth())){
            throw new CardNotValidException("Card is expired!");
        }
    }

    public static void validateUserCard(ChangeBalanceDto changeBalanceDto){
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthNumber = localDateTime.getMonthValue();
        int yearNumber = localDateTime.getYear();
        if((yearNumber > changeBalanceDto.getExpYear())){
            throw new CardNotValidException("Card is expired!");
        }
        else if((yearNumber == changeBalanceDto.getExpYear() && monthNumber > changeBalanceDto.getExpMonth())){
            throw new CardNotValidException("Card is expired!");
        }
    }


}
