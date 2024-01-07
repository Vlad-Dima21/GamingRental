package com.vladima.gamingrental.services;

import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import com.vladima.gamingrental.device.services.DeviceBaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceBaseServiceTests {

    @Mock
    private DeviceBaseRepository repository;

    @InjectMocks
    private DeviceBaseServiceImpl service;

    private DeviceBase ps5;
    private DeviceBase xbox;

    @BeforeEach
    public void init() {
        ps5 = new DeviceBase(
            1L, "PS5", "Sony",
                2018, List.of(),
                List.of()
        );
        xbox = new DeviceBase(
            2L, "Xbox Series X", "Microsoft",
                2019, List.of(new Device(3, true)),
                List.of()
        );
    }

    @Test
    @DisplayName("Unit test for fetching a nonexistent device by id that throws an error")
    public void whenInvalidId_getById_throwsEntityOperationException() {
        var deviceId = ps5.getDeviceBaseId();
        var serviceException = new EntityOperationException(
            "Device base not found",
            MessageFormat.format("Error fetching device with id {0}", deviceId),
            HttpStatus.NOT_FOUND
        );
        given(repository.findById(deviceId))
                .willReturn(Optional.empty());

        var exception = assertThrows(EntityOperationException.class,
                () -> service.getById(deviceId));
        assertEquals(serviceException.getExtraInfo(), exception.getExtraInfo());
        assertEquals(serviceException.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("Unit test for fetching a device by id that returns said device")
    public void whenValidId_getById_returnsDeviceBase() {
        given(repository.findById(ps5.getDeviceBaseId()))
                .willReturn(Optional.of(ps5));

        var returnedDevice = service.getById(ps5.getDeviceBaseId());
        assertEquals(returnedDevice.getDeviceBaseName(), ps5.getDeviceBaseName());
    }

    @Test
    @DisplayName("Unit test for fetching a device by all its fields, without accounting for available units, that returns said device")
    public void whenFilteredByNameProducerYear_getFiltered_returnsDeviceBase() {
        given(repository.findByDeviceBaseNameContaining(ps5.getDeviceBaseName()))
                .willReturn(List.of(ps5));
        given(repository.findByDeviceBaseProducer(ps5.getDeviceBaseProducer()))
                .willReturn(List.of(ps5));
        given(repository.findByDeviceBaseYearOfReleaseGreaterThanEqual(ps5.getDeviceBaseYearOfRelease()))
                .willReturn(List.of(ps5, xbox));

        var returnedDevices = service.getFiltered(ps5.getDeviceBaseName(), ps5.getDeviceBaseProducer(), ps5.getDeviceBaseYearOfRelease(),false);
        assertIterableEquals(List.of(ps5.toDTO()), returnedDevices);
    }

    @Test
    @DisplayName("Unit test for fetching devices by availability that returns the one with available units")
    public void whenFilteredByAvailability_getFiltered_returnsDeviceBase() {
        given(repository.findAll())
                .willReturn(List.of(ps5, xbox));

        var returnedDevices = service.getFiltered(null, null, null, true);
        assertIterableEquals(List.of(xbox.toDTO()), returnedDevices);
    }

    @Test
    @DisplayName("Unit test for adding a device with a taken name that throws an error")
    public void whenNewDeviceWithExistingName_create_throwsEntityOperationException() {
        var serviceException = new EntityOperationException(
            "Device not added",
            "A device with the same name already exists",
            HttpStatus.BAD_REQUEST
        );

        given(repository.findByDeviceBaseName(ps5.getDeviceBaseName()))
                .willReturn(ps5);

        var exception = assertThrows(EntityOperationException.class,
                () -> service.create(ps5.toDTO()));

        assertEquals(serviceException, exception);
    }

    @Test
    @DisplayName("Unit test for adding a device that returns the new device")
    public void whenNewDevice_create_returnsNewDeviceBase() {
        given(repository.findByDeviceBaseName(ArgumentMatchers.anyString()))
                .willReturn(null);

        given(repository.save(ArgumentMatchers.any()))
                .willReturn(ps5);

        assertEquals(ps5.toDTO(), service.create(ps5.toDTO()));
    }
}
