package com.vladima.gamingrental.client.models;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.helpers.BaseModel;
import com.vladima.gamingrental.security.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Client implements BaseModel<ClientDTO> {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long clientId;

    @Column(nullable = false)
    private String clientName;

    @Column(unique = true, nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private String clientPhone;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_user_id")
    private User clientUser;

    @OneToMany(mappedBy = "rentalClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> clientRentals;

    public Client(String clientName, String clientEmail, String clientPhone) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }

    public Client(String userName, String userEmail, String userPhone, User user) {
        this.clientName = userName;
        this.clientEmail = userEmail;
        this.clientPhone = userPhone;
        this.clientUser = user;
    }

    public Client(String clientName, String clientEmail, String clientPhone, User clientUser, List<Rental> clientRentals) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
        this.clientUser = clientUser;
        this.clientRentals = clientRentals;
    }

    @Override
    public ClientDTO toDTO() {
        return new ClientDTO(clientName, clientEmail, clientPhone);
    }
}
