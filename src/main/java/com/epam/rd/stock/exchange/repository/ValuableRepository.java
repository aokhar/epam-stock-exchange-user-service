package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Valuable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ValuableRepository extends JpaRepository<Valuable, String> {
    List<Valuable> findAll();

    @Query("SELECT v FROM Valuable v WHERE v.symbol LIKE %:symbol%")
    Page<Valuable> findStocksBySymbol(@Param("symbol") String symbol, Pageable pageable);

}
