package com.vladima.gamingrental.games.models;

import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class GameCopy implements BaseModel<GameCopyDTO>{

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

    public GameCopy(Game gameBase, DeviceBase gameDevice, boolean isAvailable) {
        this.isAvailable = isAvailable;
        this.gameBase = gameBase;
        this.gameDevice = gameDevice;
    }

    @Override
    public GameCopyDTO toDTO() {
        return new GameCopyDTO(gameBase.toDTO(), gameDevice.toDTO(), isAvailable);
    }
}
