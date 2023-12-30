package com.vladima.gamingrental.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceBaseExtrasDTO extends DeviceBaseDTO{
    private int noOfUnitsAvailable;

    public DeviceBaseExtrasDTO(@NotBlank(message = "Device must have a name") @Size(min = 2, message = "Device name must be longer") String deviceBaseName, @NotBlank @Size(min = 2, message = "The name of the manufacturer must be longer") String deviceBaseProducer, int deviceBaseYearOfRelease, int noOfUnitsAvailable) {
        super(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
        this.noOfUnitsAvailable = noOfUnitsAvailable;
    }

    public DeviceBaseExtrasDTO() {
    }
}
