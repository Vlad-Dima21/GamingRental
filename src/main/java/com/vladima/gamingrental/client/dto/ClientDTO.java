package com.vladima.gamingrental.client.dto;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.helpers.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDTO implements BaseDTO<Client> {

    @NotBlank(message = "Client must have a name")
    @Size(min = 3, max = 20, message = "Client name should be 3-20 characters long")
    private String clientName;

    @NotBlank
    @Email(message = "Invalid email", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String clientEmail;

    @NotBlank
    @Pattern(message = "Invalid phone number", regexp = "^(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$")
    private String clientPhone;

    public Client toModel() {
        return new Client(clientName, clientEmail, clientPhone);
    }
}
