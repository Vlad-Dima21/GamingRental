package com.vladima.gamingrental.security.dto;

import com.vladima.gamingrental.helpers.BaseDTO;
import com.vladima.gamingrental.security.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDTO implements BaseDTO<User> {

    @NotBlank
    @Email(message = "Invalid email", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String userEmail;

    @NotBlank
    @Size(min = 3, max = 30, message = "The password should be 3-30 characters long")
    private String userPassword;

    @Override
    public User toModel() {
        return new User(userPassword, userEmail);
    }
}
