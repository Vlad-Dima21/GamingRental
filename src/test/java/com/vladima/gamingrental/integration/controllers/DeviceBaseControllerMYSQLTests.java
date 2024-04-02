package com.vladima.gamingrental.integration.controllers;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.helpers.StringifyJSON;
import com.vladima.gamingrental.security.dto.AdminDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceBaseControllerMYSQLTests extends BaseControllerMYSQLTests{

    @Autowired
    private DeviceBaseRepository repository;

    private DeviceBase sampledDevice;

    @Before
    public void init() throws Exception {
        var random = new Random();
        var models = repository.findAll();
        sampledDevice = models.get(random.nextInt(models.size()));
        retrieveAdminToken(new AdminDTO("admin", "admin"));
    }

    @Test
    @DisplayName("Integration test for creating a new device with an already used name that returns an error json")
    public void  whenExistingDeviceName_createDevice_returnErrorJSON() throws Exception {
        var dto = new DeviceBaseDTO(sampledDevice.getDeviceBaseName(), sampledDevice.getDeviceBaseProducer(), sampledDevice.getDeviceBaseYearOfRelease());
        var exception = new EntityOperationException(
                "Device not added",
                "A device with the same name already exists",
                HttpStatus.CONFLICT
        );
        mockMvc.perform(post("/api/devices/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringifyJSON.toJSON(dto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }
}
