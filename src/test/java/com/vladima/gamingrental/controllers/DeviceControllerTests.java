package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.device.controllers.DeviceController;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.services.DeviceServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import com.vladima.gamingrental.security.configurations.TestConfigurationSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DeviceController.class)
@ContextConfiguration(classes = DeviceController.class)
@Import({TestConfigurationSecurity.class, EntitiesExceptionHandler.class})
public class DeviceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceServiceImpl service;

    private DeviceBase ps5;

    private Device ps5Unit1;

    @BeforeEach
    public void init() {
        ps5 = new DeviceBase(
                1L, "PS5", "Sony",
                2018, List.of(),
                List.of()
        );
        ps5Unit1 = new Device(1L, 1, true, ps5, null);
        Device ps5Unit2 = new Device(2L, 3, false, ps5, null);
        ps5.setDevices(List.of(ps5Unit1, ps5Unit2));
    }

    @Test
    @DisplayName("Unit test for fetching units based on the device id")
    public void whenInvalidId_getByDeviceBaseId_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Device not found",
                "Error fetching device with id 1",
                HttpStatus.NOT_FOUND
        );
        given(service.getByDeviceBaseId(ps5.getDeviceBaseId()))
                .willThrow(exception);

        mockMvc.perform(get("/api/units/of-id/{id}", ps5.getDeviceBaseId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @DisplayName("Unit test for fetching units that are available")
    public void whenFilteredByAvailability_getByDeviceBaseName_returnUnitsJSON() throws Exception {
        given(service.getByDeviceBaseName(ps5.getDeviceBaseName(), true))
                .willReturn(List.of(ps5Unit1.toDTO()));

        mockMvc.perform(get("/api/units/of")
                        .param("name", ps5.getDeviceBaseName())
                        .param("availableOnly", String.valueOf(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deviceName").value(ps5.getDeviceBaseName()));
    }
}
