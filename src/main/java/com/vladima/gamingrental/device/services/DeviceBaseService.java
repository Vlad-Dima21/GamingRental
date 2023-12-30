package com.vladima.gamingrental.device.services;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface DeviceBaseService extends BaseService<DeviceBase, DeviceBaseExtrasDTO> {
    List<DeviceBaseExtrasDTO> getByName(String name);
    DeviceBase getByExactName(String name);
    List<DeviceBaseExtrasDTO> getByProducer(String producer);
    List<DeviceBaseExtrasDTO> getByYear(int year);
    List<DeviceBaseExtrasDTO> getFiltered(String name, String producer, Integer year, boolean ifAvailable);
}
