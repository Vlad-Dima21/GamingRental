package com.vladima.gamingrental.integration.services;

import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.repositories.DeviceRepository;
import com.vladima.gamingrental.device.services.DeviceService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("mysql")
@Transactional
public class DeviceServiceMYSQLTests {

    @Autowired
    private DeviceRepository repository;

    @Autowired
    private DeviceService service;

    private Device sampledDevice;

    @BeforeEach
    public void init() {
        Random random = new Random();
        var devices = repository.findAll();
        sampledDevice = devices.get(random.nextInt(devices.size()));
    }

    @Test
    @DisplayName("Integration test for fetching the units for a given device by its id that returns the list of units")
    public void givenDeviceBaseId_getByDeviceBaseId_returnDevices() {
        var deviceBase = sampledDevice.getDeviceBase();
        var siblings = deviceBase.getDevices();
        assertEquals(siblings.stream().map(Device::toDTO).toList(), service.getByDeviceBaseId(deviceBase.getDeviceBaseId()));
    }

    @Test
    @DisplayName("Unit test for fetching the units for a given device by its name that returns the list of available units")
    public void givenDeviceBaseName_getByDeviceBaseName_returnsAvailableUnits() {
        var deviceBase = sampledDevice.getDeviceBase();
        var expectedUnits = deviceBase.getDevices().stream()
                .filter(Device::isDeviceAvailable)
                .map(Device::toDTO)
                .toList();

        assertEquals(expectedUnits, service.getByDeviceBaseName(deviceBase.getDeviceBaseName(), true));
    }
}
