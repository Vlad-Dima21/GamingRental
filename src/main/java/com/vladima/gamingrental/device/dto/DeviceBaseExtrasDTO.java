package com.vladima.gamingrental.device.dto;

import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.dto.GameCopyExtrasDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceBaseExtrasDTO extends DeviceBaseDTO{

    private Long deviceBaseId;

    private int noOfUnitsAvailable;

    private String deviceBaseImageUrl;

    private List<DeviceExtrasDTO> deviceBaseUnits;

    private List<GameCopyExtrasDTO> deviceBaseGameCopies;

    public DeviceBaseExtrasDTO( Long deviceBaseId, String deviceBaseName, String deviceBaseProducer, int deviceBaseYearOfRelease, int noOfUnitsAvailable, String deviceBaseImageUrl, List<DeviceExtrasDTO> deviceBaseUnits, List<GameCopyExtrasDTO> deviceBaseGameCopies) {
        super(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
        this.deviceBaseId = deviceBaseId;
        this.noOfUnitsAvailable = noOfUnitsAvailable;
        this.deviceBaseImageUrl = deviceBaseImageUrl;
        this.deviceBaseUnits = deviceBaseUnits;
        this.deviceBaseGameCopies = deviceBaseGameCopies;
    }

    public DeviceBaseExtrasDTO() {
    }

    public DeviceBaseExtrasDTO(@NotBlank(message = "Device must have a name") @Size(min = 2, message = "Device name must be longer") String deviceBaseName, @NotBlank @Size(min = 2, message = "The name of the manufacturer must be longer") String deviceBaseProducer, int deviceBaseYearOfRelease) {
        super(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
    }
}
