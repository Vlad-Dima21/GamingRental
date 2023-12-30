package com.vladima.gamingrental.device.dto;

import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.helpers.BaseDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceDTO implements BaseDTO<Device> {

    @Min(0)
    private int deviceNumberOfControllers;

    private boolean deviceIsAvailable;

    @Override
    public Device toModel() {
        return new Device(deviceNumberOfControllers, deviceIsAvailable);
    }
}
