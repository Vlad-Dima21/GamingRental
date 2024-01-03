package com.vladima.gamingrental.games.dto;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.BaseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameCopyDTO implements BaseDTO<GameCopy> {

    @NotNull
    @Valid
    private GameDTO gameBase;

    @Valid
    private DeviceBaseDTO gameDevice;

    @NotNull
    private boolean isAvailable;
    @Override
    public GameCopy toModel() {
        return new GameCopy(gameBase.toModel(), gameDevice.toModel(), isAvailable);
    }
}
