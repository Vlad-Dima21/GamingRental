package com.vladima.gamingrental.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceBaseExtrasDTO extends DeviceBaseDTO{
    private int noOfUnitsAvailable;

    private String deviceBaseImageUrl;

    public DeviceBaseExtrasDTO(@NotBlank(message = "Device must have a name") @Size(min = 2, message = "Device name must be longer") String deviceBaseName, @NotBlank @Size(min = 2, message = "The name of the manufacturer must be longer") String deviceBaseProducer, int deviceBaseYearOfRelease, int noOfUnitsAvailable, String deviceBaseImageURL) {
        super(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
        this.noOfUnitsAvailable = noOfUnitsAvailable;
        this.deviceBaseImageUrl = deviceBaseImageURL;
    }

    public DeviceBaseExtrasDTO() {
    }

    public DeviceBaseExtrasDTO(@NotBlank(message = "Device must have a name") @Size(min = 2, message = "Device name must be longer") String deviceBaseName, @NotBlank @Size(min = 2, message = "The name of the manufacturer must be longer") String deviceBaseProducer, int deviceBaseYearOfRelease) {
        super(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
    }
}
