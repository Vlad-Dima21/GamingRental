package com.vladima.gamingrental.client.dto;

import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.device.dto.DeviceDTO;
import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.helpers.BaseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalDTO implements BaseDTO<Rental> {

    @Future
    @NotNull
    private LocalDateTime rentalDueDate;

    private LocalDateTime rentalReturnDate;

    @NotNull
    @Valid
    private ClientDTO rentalClient;

    @Valid
    private DeviceDTO rentalDevice;

    @Valid
    private List<GameCopyDTO> rentalGames;
    @Override
    public Rental toModel() {
        return new Rental(rentalDueDate, rentalReturnDate, rentalClient.toModel(), rentalDevice.toModel(),
            rentalGames.stream().map(GameCopyDTO::toModel).toList());
    }
}
