package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.exception.EntityAlreadyExistsException;
import com.epam.rd.stock.exchange.exception.UserNotFoundException;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " is not registered"));
    }

    @Override
    public void updateBlockingStatus(String email, boolean isBlocked) {
        User user = findByEmail(email);
        user.setBlocked(isBlocked);
        userRepository.save(user);
    }

    @Override
    public void updateRole(String email, UserRole userRole) {
        User user = findByEmail(email);
        user.setRole(userRole);
        userRepository.save(user);
    }

    @Override
    public Page<User> findAllUsersByEmail(String email, Pageable pageable) {
        return userRepository.findAllUsersByEmail(email, pageable);
    }

    @Override
    public User save(User user) {
        if (isUserExists(user.getEmail())) {
            throw new EntityAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public UserRole getUserRole(String email) {
        User user = userRepository.findByActualEmail(userMapper.toEmailWithoutDots(email))
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " is not registered"));
        return user.getRole();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByActualEmail(userMapper.toEmailWithoutDots(email))
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " is not registered"));
    }

    private boolean isUserExists(String email) {
        return userRepository.findByActualEmail(userMapper.toEmailWithoutDots(email)).isPresent();
    }

}
