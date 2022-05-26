package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.UserValuableInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserValuableInfoRepository extends JpaRepository<UserValuableInfo, String> {
    Optional<UserValuableInfo> findByUserIdAndValuableId(String userId, String stockId);

    List<UserValuableInfo> findByUserId(String userId);

}
