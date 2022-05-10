package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface UserService {

   User save(User user);

   User findByEmail(String email);

   UserRole getUserRole(String email);

   User findById(String id);

   void updateBlockingStatus(String email, boolean isBlocked);

   void updateRole(String email, UserRole userRole);

   Page<User> findAllUsersByEmail(String email, Pageable pageable);

}
