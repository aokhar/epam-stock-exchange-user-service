package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {
    List<Stock> findAll();

    @Query("SELECT s FROM Stock s WHERE s.symbol LIKE %:symbol%")
    Page<Stock> findStocksBySymbol(@Param("symbol") String symbol, Pageable pageable);
}
