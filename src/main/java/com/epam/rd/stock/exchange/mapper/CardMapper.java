package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.NewCardDto;
import com.epam.rd.stock.exchange.dto.UpdateCardDto;
import com.epam.rd.stock.exchange.dto.UserCardViewDto;
import com.epam.rd.stock.exchange.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    public UserCardViewDto toUserView(Card card){
        return UserCardViewDto.builder()
                .id(card.getId())
                .card(card.getCard())
                .cardHolder(card.getCardHolder())
                .cvc(card.getCvc())
                .expMonth(card.getExpMonth())
                .expYear(card.getExpYear())
                .build();
    }

    public Page<UserCardViewDto> toUserView(Page<Card> cards){
        return cards.map(this::toUserView);
    }

    public Card toCardUpdate(Card card, UpdateCardDto updateCardDto){
        card.setCard(updateCardDto.getCard());
        card.setCardHolder(updateCardDto.getCardHolder());
        card.setCvc(updateCardDto.getCvc());
        card.setExpMonth(updateCardDto.getExpMonth());
        card.setExpYear(updateCardDto.getExpYear());
        return card;
    }

    public Card toCard(NewCardDto newCardDto){
        return Card.builder()
                .card(newCardDto.getCard())
                .cardHolder(newCardDto.getCardHolder())
                .cvc(newCardDto.getCvc())
                .expMonth(newCardDto.getExpMonth())
                .expYear(newCardDto.getExpYear())
                .build();
    }
}
