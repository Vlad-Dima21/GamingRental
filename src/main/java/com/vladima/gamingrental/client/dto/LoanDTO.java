package com.vladima.gamingrental.client.dto;

import com.vladima.gamingrental.client.models.Loan;
import com.vladima.gamingrental.device.dto.DeviceDTO;
import com.vladima.gamingrental.helpers.BaseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanDTO implements BaseDTO<Loan> {

    @Future
    @NotNull
    private LocalDateTime loanDueDate;

    private LocalDateTime loanReturnDate;

    @NotNull
    @Valid
    private ClientDTO loanClient;

    @Valid
    private DeviceDTO loanDevice;
    @Override
    public Loan toModel() {
        return new Loan(loanDueDate, loanReturnDate, loanClient.toModel(), loanDevice.toModel());
    }
}
