package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.CardNotFoundException;
import com.epam.rd.stock.exchange.exception.UserDontHaveAccessToThisCard;
import com.epam.rd.stock.exchange.model.Card;
import com.epam.rd.stock.exchange.repository.CardRepository;
import com.epam.rd.stock.exchange.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public Page<Card> findByUserId(String userId, Pageable pageable) {
        return cardRepository.findByUserId(userId, null);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void delete(String cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public Card findByUserIdAndCardId(String userId, String cardId) {
       Optional<Card> cardOptional = cardRepository.findById(cardId);
       if(cardOptional.isEmpty()){
           throw new CardNotFoundException("Card not found");
       }
       Card card = cardOptional.get();
       if(!card.getUserId().equals(userId)){
           throw new UserDontHaveAccessToThisCard("This is not current user card");
       }
       return card;
    }

    @Override
    public boolean checkCardExist(String userId, Card card) {
        Optional<Card> cardOptional = cardRepository.findByUserIdAndCardAndCardHolderAndCvcAndExpMonthAndExpYear(userId,
                card.getCard(), card.getCardHolder(), card.getCvc(), card.getExpMonth(), card.getExpYear());
        return cardOptional.isPresent();
    }

}
