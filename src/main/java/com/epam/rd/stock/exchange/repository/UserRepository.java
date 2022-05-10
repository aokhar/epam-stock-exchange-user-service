package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByActualEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:email%")
    Page<User> findAllUsersByEmail(@Param("email") String email, Pageable pageable);
}
