package com.vladima.gamingrental.games.models;

import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.dto.GameCopyExtrasDTO;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class GameCopy implements BaseModel<GameCopyExtrasDTO>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameCopyId;

    @Column(nullable = false)
    private boolean isAvailable = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game gameBase;

    @ManyToOne
    @JoinColumn(name = "device_base_id")
    private DeviceBase gameDevice;

    @ManyToMany(mappedBy = "rentalGames", cascade = CascadeType.ALL)
    private List<Rental> gameRentals;

    public GameCopy(Game gameBase, DeviceBase gameDevice, boolean isAvailable) {
        this.isAvailable = isAvailable;
        this.gameBase = gameBase;
        this.gameDevice = gameDevice;
    }

    public GameCopy(boolean isAvailable, Game gameBase, DeviceBase gameDevice, List<Rental> gameRentals) {
        this.isAvailable = isAvailable;
        this.gameBase = gameBase;
        this.gameDevice = gameDevice;
        this.gameRentals = gameRentals;
    }

    @Override
    public GameCopyExtrasDTO toDTO() {
        return new GameCopyExtrasDTO(gameBase.getGameId(), gameCopyId, gameDevice.getDeviceBaseId(), gameBase.toDTO(), gameDevice.toBaseDTO(), isAvailable);
    }
}
