package com.vladima.gamingrental.device.services;

import com.vladima.gamingrental.device.dto.DeviceExtrasDTO;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.repositories.DeviceRepository;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl extends BaseServiceImpl<Device, DeviceExtrasDTO, DeviceRepository> implements DeviceService {

    private final DeviceBaseService deviceBaseService;

    public DeviceServiceImpl(DeviceRepository repository, DeviceBaseService deviceBaseService) {
        super(repository);
        this.deviceBaseService = deviceBaseService;
    }

    @Override
    public List<DeviceExtrasDTO> getByDeviceBaseId(Long id) {
        // make sure deviceBase exists
        deviceBaseService.getById(id);
        return getRepository().findByDeviceBaseId(id)
                .stream()
                .map(Device::toDTO)
                .toList();
    }

    @Override
    public List<DeviceExtrasDTO> getByDeviceBaseName(String name) {
        deviceBaseService.getByExactName(name);
        return getRepository().findByDeviceBaseName(name).stream().map(Device::toDTO).toList();
    }

    @Override
    @Transactional
    public DeviceExtrasDTO updateDeviceAvailability(Long id, boolean isAvailable) {
        var unit = getModelById(id);
        unit.setDeviceAvailable(isAvailable);
        return unit.toDTO();
    }

    @Override
    public DeviceExtrasDTO create(DeviceExtrasDTO deviceExtrasDTO) {
        var unit = deviceExtrasDTO.toModel();
        var device = deviceBaseService.getByExactName(deviceExtrasDTO.getDeviceName());
        return getRepository().save(
            new Device(
                deviceExtrasDTO.getDeviceNumberOfControllers(),
                deviceExtrasDTO.isDeviceIsAvailable(),
                device
            )
        ).toDTO();
    }

    @Override
    public DeviceExtrasDTO updateInfo(Long id, DeviceExtrasDTO deviceExtrasDTO) {
        return null;
    }

    @Override
    public String getLogName() {
        return "Unit";
    }
}
