package com.vladima.gamingrental.services;

import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceRepository;
import com.vladima.gamingrental.device.services.DeviceBaseService;
import com.vladima.gamingrental.device.services.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTests {

    @Mock
    private DeviceRepository repository;

    @Mock
    private DeviceBaseService deviceBaseService;

    @InjectMocks
    private DeviceServiceImpl service;

    private DeviceBase ps5;

    private Device ps5Unit1;
    private Device ps5Unit2;

    @BeforeEach
    public void init() {
        ps5 = new DeviceBase(
                1L, "PS5", "Sony",
                2018, List.of(),
                List.of()
        );
        ps5Unit1 = new Device(1L, 1, true, ps5, null);
        ps5Unit2 = new Device(2L, 3, false, ps5, null);
        ps5.setDevices(List.of(ps5Unit1, ps5Unit2));
    }

    @Test
    @DisplayName("Unit test for fetching the units for a given device by its id that returns the list of units")
    public void givenDeviceBaseId_getByDeviceBaseId_returnDevices() {
        given(repository.findByDeviceBaseId(ps5.getDeviceBaseId()))
                .willReturn(ps5.getDevices());

        assertEquals(List.of(ps5Unit1.toDTO(), ps5Unit2.toDTO()), service.getByDeviceBaseId(ps5.getDeviceBaseId()));
    }

    @Test
    @DisplayName("Unit test for fetching the units for a given device by its name that returns the list of available units")
    public void givenDeviceBaseName_getByDeviceBaseName_returnsAvailableUnits() {
        given(repository.findByDeviceBaseName(ps5.getDeviceBaseName()))
                .willReturn(ps5.getDevices());

        var availableUnits = service.getByDeviceBaseName(ps5.getDeviceBaseName(), true);

        assertEquals(1, availableUnits.size());
        assertEquals(ps5.getDeviceBaseName(), availableUnits.get(0).getDeviceName());
    }
}
