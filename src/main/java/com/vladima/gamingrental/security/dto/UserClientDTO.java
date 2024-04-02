package com.vladima.gamingrental.security.dto;

import com.vladima.gamingrental.helpers.BaseDTO;
import com.vladima.gamingrental.security.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserClientDTO extends UserDTO {

    public UserClientDTO(@NotBlank @Email(message = "Invalid email", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") String userEmail, @NotBlank @Size(min = 3, max = 30, message = "The password should be 3-30 characters long") String userPassword, String userName, String userPhone) {
        super(userEmail, userPassword);
        this.userName = userName;
        this.userPhone = userPhone;
    }

    @NotBlank(message = "User must have a name")
    @Size(min = 3, max = 20, message = "User name should be 3-20 characters long")
    private String userName;

    @NotBlank(message = "User must have a phone number")
    @Pattern(message = "Invalid phone number", regexp = "^(07[0-8][0-9]|02[0-9]{2}|03[0-9]{2})(\\s|\\.|-)?([0-9]{3}(\\s|\\.|-|)){2}$")
    private String userPhone;

    @Override
    public User toModel() {
        return new User(getUserPassword(), getUserEmail(), userName, userPhone);
    }
}
