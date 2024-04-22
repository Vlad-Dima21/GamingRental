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

    private Long deviceId;

    private Long deviceBaseId;

    @NotEmpty
    private String deviceName;

    public DeviceExtrasDTO(@Min(0) int deviceNumberOfControllers, boolean deviceIsAvailable, Long deviceId, Long deviceBaseId, String deviceName) {
        super(deviceNumberOfControllers, deviceIsAvailable);
        this.deviceId = deviceId;
        this.deviceBaseId = deviceBaseId;
        this.deviceName = deviceName;
    }
}
