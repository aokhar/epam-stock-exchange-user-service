package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.UserStockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStockInfoRepository extends JpaRepository<UserStockInfo, String> {
    Optional<UserStockInfo> findByUserIdAndStockId(String userId, String stockId);

    List<UserStockInfo> findByUserId(String userId);
}
