package com.epam.rd.stock.exchange.service.integration;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import com.epam.rd.stock.exchange.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserServiceIT extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService sut;

    private User user;

    @BeforeEach
    public void init() {
        user = EntityGenerator.generateDomainUser();
    }

    @Test
    public void shouldSaveUserInRepository() {
        // Given

        // When
        sut.save(user);

        // Then
        User actualSavedUser = userRepository.findByActualEmail(user.getActualEmail()).orElse(null);
        assertNotNull(actualSavedUser);
        assertEquals(user.getFirstName(), actualSavedUser.getFirstName());
        assertEquals(user.getLastName(), actualSavedUser.getLastName());
        assertEquals(user.getEmail(), actualSavedUser.getEmail());
        assertEquals(user.getActualEmail(), actualSavedUser.getActualEmail());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenFindByEmail() {
        // Given
        userRepository.delete(user);

        // When

        // Then
        assertThrows(UserNotFoundException.class,
                () -> sut.findByEmail(user.getEmail()));
    }

    @Test
    public void shouldFindUserById() {
        // Given
        user = sut.save(user);

        // When
        User result = sut.findById(user.getId());

        // Then
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getActualEmail(), result.getActualEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenFindUserById() {
        // Given
        userRepository.delete(user);

        // When

        // Then
        assertThrows(UserNotFoundException.class,
                () -> sut.findById(user.getId()));
    }
}
