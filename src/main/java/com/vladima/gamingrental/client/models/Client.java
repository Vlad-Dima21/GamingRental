package com.vladima.gamingrental.client.models;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Client implements BaseModel<ClientDTO> {

    @Id
    @SequenceGenerator(
        name = "client_sequence",
        sequenceName = "client_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "client_sequence"
    )
    private Long clientId;

    @Column(nullable = false)
    private String clientName;

    @Column(unique = true, nullable = false)
    private String clientEmail;

    @Column(unique = true, nullable = false)
    private String clientPhone;

    public Client(String clientName, String clientEmail, String clientPhone) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }


    @Override
    public ClientDTO toDTO() {
        return new ClientDTO(clientName, clientEmail, clientPhone);
    }
}
