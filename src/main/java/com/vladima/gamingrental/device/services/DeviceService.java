package com.vladima.gamingrental.device.services;

import com.vladima.gamingrental.device.dto.DeviceExtrasDTO;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface DeviceService extends BaseService<Device, DeviceExtrasDTO> {
    List<DeviceExtrasDTO> getByDeviceBaseId(Long id);
    List<DeviceExtrasDTO> getByDeviceBaseName(String name, boolean availableOnly);
    DeviceExtrasDTO updateDeviceAvailability(Long id, boolean isAvailable);
    Device getModelById(Long id, boolean shouldBeAvailable);
}
