package com.vladima.gamingrental.device.dto;


import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.helpers.BaseDTO;
import com.vladima.gamingrental.helpers.validators.ValidateYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceBaseDTO implements BaseDTO<DeviceBase> {

    @NotBlank(message = "Device must have a name")
    @Size(min = 2, message = "Device name must be longer")
    private String deviceBaseName;

    @NotBlank
    @Size(min = 2, message = "The name of the manufacturer must be longer")
    private String deviceBaseProducer;

    @ValidateYear
    private int deviceBaseYearOfRelease;

    @Override
    public DeviceBase toModel() {
        return new DeviceBase(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
    }

    public DeviceBaseExtrasDTO toExtended() {
        return new DeviceBaseExtrasDTO(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
    }
}
