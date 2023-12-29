package com.vladima.gamingrental.client.models;

import com.vladima.gamingrental.device.models.Device;
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
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;
    private LocalDateTime loanDueDate;
    private LocalDateTime loanReturnDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private Client loanClient;
    @ManyToOne(cascade = CascadeType.ALL)
    private Device loanDevice;
}
