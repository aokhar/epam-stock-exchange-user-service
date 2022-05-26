package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.NewCardDto;
import com.epam.rd.stock.exchange.dto.UpdateCardDto;
import com.epam.rd.stock.exchange.dto.UserCardViewDto;
import com.epam.rd.stock.exchange.exception.CardExistException;
import com.epam.rd.stock.exchange.facade.CardFacade;
import com.epam.rd.stock.exchange.mapper.CardMapper;
import com.epam.rd.stock.exchange.model.Card;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.service.CardService;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.util.CardValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardFacadeImpl implements CardFacade {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final UserService userService;

    @Override
    public Page<UserCardViewDto> findCardsByUserEmail(String email, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        User user = userService.findByEmail(email);
        Page<Card> cards = cardService.findByUserId(user.getId(), pageable);
        return cardMapper.toUserView(cards);
    }

    @Override
    public UserCardViewDto getCardById(String email, String cardId) {
        User user = userService.findByEmail(email);
        String userId = user.getId();
        Card card = cardService.findByUserIdAndCardId(userId, cardId);
        return cardMapper.toUserView(card);
    }

    @Override
    public UserCardViewDto updateCard(String email, UpdateCardDto updateCardDto) {
        CardValidationUtil.validateUserCard(updateCardDto);
        User user = userService.findByEmail(email);
        Card card = cardService.findByUserIdAndCardId(user.getId(), updateCardDto.getCardId());
        Card newCard = cardMapper.toCardUpdate(card, updateCardDto);
        if(cardService.checkCardExist(user.getId(), newCard)){
            throw new CardExistException("User already have this card");
        }
        card = cardService.save(newCard);
        return cardMapper.toUserView(card);
    }

    @Override
    public void addCard(String email, NewCardDto newCardDto) {
        CardValidationUtil.validateUserCard(newCardDto);
        Card newCard = cardMapper.toCard(newCardDto);
        User user = userService.findByEmail(email);
        if(cardService.checkCardExist(user.getId(), newCard)){
            throw new CardExistException("User already have this card");
        }
        newCard.setUserId(user.getId());
        cardService.save(newCard);
    }

    @Override
    public Long removeCard(String email, String cardId) {
        User user = userService.findByEmail(email);
        Card card = cardService.findByUserIdAndCardId(user.getId(), cardId);
        cardService.delete(card.getId());
        return card.getCard();
    }

}
