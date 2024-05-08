package com.vladima.gamingrental.integration.services;

import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import com.vladima.gamingrental.device.services.DeviceBaseService;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.text.MessageFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("mysql")
@Transactional
public class DeviceBaseServiceMYSQLTests {

    @Autowired
    private DeviceBaseRepository repository;

    @Autowired
    private DeviceBaseService service;

    private List<DeviceBase> testingDevices;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        testingDevices = repository.saveAll(List.of(
            new DeviceBase("PS4 Slim", "Sony", 2014),
            new DeviceBase("Xbox 360", "Microsoft", 2005),
            new DeviceBase("Xbox One", "Microsoft", 2014),
            new DeviceBase("PSVita", "Sony", 2011)
        ));
    }

    @Test
    @DisplayName("Integration test for fetching a nonexistent device by id that throws an error")
    public void whenInvalidId_getById_throwsEntityOperationException() {
        var maxLong = Long.MAX_VALUE;
        var expected = new EntityOperationException(
                "Device base not found",
                MessageFormat.format("Error fetching device with id {0}", maxLong),
                HttpStatus.NOT_FOUND
        );
        var exception = assertThrows(EntityOperationException.class, () -> service.getById(maxLong));
        assertEquals(expected, exception);
    }

    @Test
    @DisplayName("Integration test for fetching a device by id that returns said device")
    public void whenValidId_getById_returnsDeviceBase() {
        var expected = testingDevices.get(0);
        var returnedDevice = service.getById(expected.getDeviceBaseId());
        assertEquals(expected.toDTO(), returnedDevice);
    }
}
