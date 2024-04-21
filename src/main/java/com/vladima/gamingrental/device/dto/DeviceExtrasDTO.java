package com.vladima.gamingrental.device.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceExtrasDTO extends DeviceDTO{

    @NotNull
    private Long deviceId;

    @NotEmpty
    private String deviceName;

    public DeviceExtrasDTO(@Min(0) int deviceNumberOfControllers, boolean deviceIsAvailable, Long deviceId, String deviceName) {
        super(deviceNumberOfControllers, deviceIsAvailable);
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }
}
