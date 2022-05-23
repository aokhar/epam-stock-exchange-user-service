package com.epam.rd.stock.exchange.mapper;

import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.dto.UserSignInDto;
import com.epam.rd.stock.exchange.dto.UserViewAdminDto;
import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toUser(UserCreateDto userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .actualEmail(toEmailWithoutDots(userDTO.getEmail()))
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .balance(new BigDecimal(0))
                .firstName(userDTO.getFirstname())
                .lastName(userDTO.getLastname())
                .role(UserRole.USER)
                .build();
    }

    public User toUserSocial(UserCreateDto userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .actualEmail(toEmailWithoutDots(userDTO.getEmail()))
                .firstName(userDTO.getFirstname())
                .balance(new BigDecimal(0))
                .lastName(userDTO.getLastname())
                .role(UserRole.USER)
                .build();
    }

    public UserSignInDto toUserSignInDto(String email, String password) {
        return UserSignInDto.builder()
                .email(email)
                .password(password)
                .build();
    }

    public UserCreateDto getUserCreateDto(DefaultOAuth2User user) {
        String name = user.getAttribute("name");
        String[] names = name.split(" ");

        return UserCreateDto.builder()
                .email(user.getAttribute("email"))
                .lastname(names[1])
                .firstname(names[0])
                .build();
    }

    public UserViewDto toUserViewDto(User user) {
        return UserViewDto.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .balance(user.getBalance())
                .email(user.getEmail())
                .isBlocked(user.isBlocked())
                .build();
    }

    public String toEmailWithoutDots(String email) {
        return email.split("@")[0].replace(".", "") + "@" + email.split("@")[1];
    }

    public UserViewAdminDto toUserViewAdminDto(User user){
        return UserViewAdminDto.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .userRole(user.getRole())
                .isBlocked(user.isBlocked())
                .build();
    }

    public Page<UserViewAdminDto> toUserViewAdminDtoPage(Page<User> users) {
        return users.map(this::toUserViewAdminDto);
    }

}
