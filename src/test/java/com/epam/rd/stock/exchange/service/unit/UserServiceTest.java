package com.epam.rd.stock.exchange.service.unit;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.exception.EntityAlreadyExistsException;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.service.UserService;
import com.epam.rd.stock.exchange.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository repository = mock(UserRepository.class);

    private UserMapper userMapper = new UserMapper(new BCryptPasswordEncoder());

    private UserService service = new UserServiceImpl(repository, userMapper);

    private User user;

    @BeforeEach
    public void init() {
        user = EntityGenerator.generateDomainUser();
    }

    @Test
    public void shouldSaveUser() {
        //Given
        when(repository.findByActualEmail(user.getActualEmail())).thenReturn(Optional.empty());
        when(repository.save(user)).thenReturn(user);

        //When
        User newUser = service.save(user);

        //Then
        Assertions.assertEquals(user, newUser);
    }

    @Test
    public void shouldThrowEntityAlreadyExistExceptionWhenSaveUserWithAlreadyExistedEmail() {
        //Given
        when(repository.findByActualEmail(user.getActualEmail())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        //When + Then
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> service.save(user));
    }


    @Test
    public void shouldReturnUserWhenFindByEmail() {
        //Given
        when(repository.findByActualEmail(user.getActualEmail())).thenReturn(Optional.of(user));

        //When
        User resultUser = service.findByEmail(user.getActualEmail());

        //Then
        Assertions.assertEquals(user, resultUser);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenFindByNotExistedEmail() {
        //Given
        when(repository.findByActualEmail(user.getActualEmail())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThrows(UserNotFoundException.class, () -> service.findByEmail(user.getActualEmail()));
    }

    @Test
    public void shouldReturnUserRoleWhenGetUserRole() {
        //Given
        when(repository.findByActualEmail(user.getActualEmail())).thenReturn(Optional.of(user));

        //When
        UserRole role = service.getUserRole(user.getActualEmail());

        //Then
        Assertions.assertEquals(UserRole.USER, role);
    }


    @Test
    public void shouldThrownUserNotFoundExceptionWhenGetUserRole() {
        //Given
        when(repository.findByActualEmail(user.getActualEmail())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThrows(UserNotFoundException.class, () -> service.getUserRole(user.getActualEmail()));
    }


    @Test
    public void shouldReturnUserWhenFindById() {
        //Given
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        //When
        User resultUser = service.findById(user.getId());

        //Then
        Assertions.assertEquals(user, resultUser);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenFindByNotExistedId() {
        //Given
        when(repository.findById(user.getId())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThrows(UserNotFoundException.class, () -> service.findByEmail(user.getActualEmail()));
    }

}
