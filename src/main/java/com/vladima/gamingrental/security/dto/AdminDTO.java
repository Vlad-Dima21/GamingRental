package com.vladima.gamingrental.security.dto;

import com.vladima.gamingrental.helpers.BaseDTO;
import com.vladima.gamingrental.security.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AdminDTO implements BaseDTO<User> {

    @NotBlank
    @Size(min = 3, max = 16, message = "Username should be between 3-16 characters long")
    private String userName;

    @NotBlank
    @Size(min = 3, max = 16, message = "Password should be between 3-16 characters long")
    private String userPassword;

    @Override
    public User toModel() {
        return new User(userName, userPassword, null);
    }
}
