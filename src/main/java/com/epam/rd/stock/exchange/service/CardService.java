package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    Page<Card> findByUserId(String userId, Pageable pageable);

    Card save(Card card);

    void delete(String cardId);

    Card findByUserIdAndCardId(String userId, String cardId);

    boolean checkCardExist(String userId, Card card);
}
