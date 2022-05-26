package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {

    Page<Card> findByUserId(String userId, Pageable pageable);

    Optional<Card> findByUserIdAndCardAndCardHolderAndCvcAndExpMonthAndExpYear(String userId, Long card, String cardHolder, Integer cvc, Integer expMonth, Integer expYear);

}
