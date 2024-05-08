package com.vladima.gamingrental.client.models;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Rental implements BaseModel<RentalDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @Column
    private LocalDateTime rentalDueDate;

    @Column
    private LocalDateTime rentalReturnDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rental_client_id")
    private Client rentalClient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rental_device_id")
    private Device rentalDevice;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "rental_game_copy",
        joinColumns = @JoinColumn(name = "rental_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "game_copy_id", nullable = false)
    )
    private List<GameCopy> rentalGames;

    public Rental(LocalDateTime rentalDueDate, LocalDateTime rentalReturnDate, Client rentalClient, Device rentalDevice, List<GameCopy> rentalGames) {
        this.rentalDueDate = rentalDueDate;
        this.rentalReturnDate = rentalReturnDate;
        this.rentalClient = rentalClient;
        this.rentalDevice = rentalDevice;
        this.rentalGames = rentalGames;
    }

    @Override
    public RentalDTO toDTO() {
        return new RentalDTO(rentalId, rentalDueDate, rentalReturnDate, rentalClient.toDTO(), rentalDevice.toDTO(),
                rentalGames.stream().map(GameCopy::toDTO).toList());
    }
}
