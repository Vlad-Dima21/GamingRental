package com.vladima.gamingrental.client.models;

import com.vladima.gamingrental.client.dto.LoanDTO;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Loan implements BaseModel<LoanDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Column
    private LocalDateTime loanDueDate;

    @Column
    private LocalDateTime loanReturnDate;

    @ManyToOne(optional = false)
    private Client loanClient;

    @ManyToOne(optional = false)
    private Device loanDevice;

    public Loan(LocalDateTime loanDueDate, LocalDateTime loanReturnDate, Client loanClient, Device loanDevice) {
        this.loanDueDate = loanDueDate;
        this.loanReturnDate = loanReturnDate;
        this.loanClient = loanClient;
        this.loanDevice = loanDevice;
    }

    @Override
    public LoanDTO toDTO() {
        return null;
    }
}
