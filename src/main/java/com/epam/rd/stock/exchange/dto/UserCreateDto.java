package com.epam.rd.stock.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Email must not be blank!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Password must not be blank!")
    @Size(min = 6,message = "Password must contain at least 6 characters!")
    private String password;

    @NotBlank(message = "Firstname must not be blank!")
    private String firstname;

    @NotBlank(message = "Lastname must not be blank!")
    private String lastname;

}
