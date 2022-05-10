package com.epam.rd.stock.exchange.facade;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.datagenerator.StringGenerator;
import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.dto.UserSignInDto;
import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.exception.AuthenticationException;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.security.config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserFacadeIT extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserFacade userFacade;

    private UserCreateDto userCreateDto;
    private User domainUser;
    private UserSignInDto userSignInDto;

    @BeforeEach
    public void init() {
        userCreateDto = EntityGenerator.generateUserCreateDto();
        userSignInDto = userMapper.toUserSignInDto(userCreateDto.getEmail(), userCreateDto.getPassword());
        domainUser = userMapper.toUser(userCreateDto);
    }

    @Test
    public void shouldSignInUser() {
        // Given
        userRepository.save(domainUser);

        // When
        UserViewDto userViewDto = userFacade.signIn(userSignInDto);

        // Then
        assertNotNull(userViewDto);
        assertEquals(domainUser.getId(), userViewDto.getId());
        assertEquals(domainUser.getEmail(), userViewDto.getEmail());
        assertEquals(domainUser.getFirstName(), userViewDto.getFirstname());
        assertEquals(domainUser.getLastName(), userViewDto.getLastname());
    }

    @Test
    public void shouldRejectSignInIfWrongPassword() {
        // Given
        userRepository.save(domainUser);
        UserSignInDto wrongUserSignInDto = userMapper.toUserSignInDto(userSignInDto.getEmail(),
                StringGenerator.generateRandomString(10));

        // When

        // Then
        assertThrows(AuthenticationException.class, () -> userFacade.signIn(wrongUserSignInDto));
    }

    @Test
    public void shouldRegisterUser() {
        // Given

        // When
        userFacade.registration(userCreateDto);

        // Then
        User actualSavedUser = (User) userRepository.findByActualEmail(userCreateDto.getEmail()).orElse(null);
        assertNotNull(actualSavedUser);
        assertEquals(userCreateDto.getFirstname(), actualSavedUser.getFirstName());
        assertEquals(userCreateDto.getLastname(), actualSavedUser.getLastName());
        assertEquals(userCreateDto.getEmail(), actualSavedUser.getEmail());
    }

    @Test
    public void shouldSaveAndAuthenticateUser() {
        // Given

        // When
        userFacade.signInWithSocialNetwork(userCreateDto);

        // Then
        User actualSavedUser = userRepository.findByActualEmail(userCreateDto.getEmail()).orElse(null);
        assertNotNull(actualSavedUser);
        assertEquals(userCreateDto.getFirstname(), actualSavedUser.getFirstName());
        assertEquals(userCreateDto.getLastname(), actualSavedUser.getLastName());
        assertEquals(userCreateDto.getEmail(), actualSavedUser.getEmail());
    }

    @Test
    public void shouldFindUserViewById() {
        // Given
        userRepository.save(domainUser);

        // When
        UserViewDto actualReceivedUserView = userFacade.findById(domainUser.getId());

        // Then
        assertNotNull(actualReceivedUserView);
        assertEquals(domainUser.getId(), actualReceivedUserView.getId());
        assertEquals(domainUser.getFirstName(), actualReceivedUserView.getFirstname());
        assertEquals(domainUser.getLastName(), actualReceivedUserView.getLastname());
        assertEquals(domainUser.getEmail(), actualReceivedUserView.getEmail());
    }
}
