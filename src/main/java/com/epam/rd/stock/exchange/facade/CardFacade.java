package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.dto.NewCardDto;
import com.epam.rd.stock.exchange.dto.UpdateCardDto;
import com.epam.rd.stock.exchange.dto.UserCardViewDto;
import org.springframework.data.domain.Page;

public interface CardFacade {

    Page<UserCardViewDto> findCardsByUserEmail(String email, Integer page, Integer pageSize);

    UserCardViewDto getCardById(String email, String cardId);

    UserCardViewDto updateCard(String email, UpdateCardDto updateCardDto);

    void addCard(String email, NewCardDto newCardDto);

    Long removeCard(String email, String cardId);

}