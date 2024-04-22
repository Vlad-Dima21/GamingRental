package com.vladima.gamingrental.games.dto;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.games.models.GameCopy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GameCopyExtrasDTO extends GameCopyDTO {
    private Long gameId;
    private Long gameCopyId;
    private Long deviceBaseId;

    public GameCopyExtrasDTO(Long gameId, Long gameCopyId, Long deviceBaseId, GameDTO gameBase, DeviceBaseDTO gameDevice, boolean isAvailable) {
        super(gameBase, gameDevice, isAvailable);
        this.gameId = gameId;
        this.gameCopyId = gameCopyId;
        this.deviceBaseId = deviceBaseId;
    }
}
