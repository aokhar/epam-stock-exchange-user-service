package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface UserService {

   User save(User user);

   User findByEmail(String email);

   UserRole getUserRole(String email);

   User findById(String id);

   void updateBalance(String user_id, BigDecimal update);
}
