package com.vladima.gamingrental.integration.controllers;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.helpers.StringifyJSON;
import com.vladima.gamingrental.security.dto.AdminDTO;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.LinkedHashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceBaseControllerMYSQLTests extends BaseControllerMYSQLTests<DeviceBase>{

    @Autowired
    private DeviceBaseRepository repository;

    @Override
    protected DeviceBaseRepository getRepository() {
        return repository;
    }

    public void init() throws Exception {
        super.init();
        retrieveAdminToken(new AdminDTO("admin", "admin"));
    }

    @Test
    @DisplayName("Integration test for creating a new device with an already used name that returns an error json")
    public void  whenExistingDeviceName_createDevice_returnErrorJSON() throws Exception {
        var dto = new DeviceBaseDTO(sampledModel.getDeviceBaseName(), sampledModel.getDeviceBaseProducer(), sampledModel.getDeviceBaseYearOfRelease());
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

    @Test
    @DisplayName("Integration test for fetching devices given a filter")
    public void whenFilteredByName_getFilteredDevices_returnDevicesJSON() throws Exception {
        var nameFilter = sampledModel.getDeviceBaseName().substring(0, 2);
        var stringResults = mockMvc.perform(get("/api/devices")
                        .param("name", nameFilter))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();
        var result = new JacksonJsonParser().parseList(stringResults)
                .stream().filter(d -> Objects.equals(((LinkedHashMap<?, ?>)d).get("deviceBaseName"), sampledModel.getDeviceBaseName())).toList();
        assertNotNull(result);
    }

    @Test
    @DisplayName("Unit test for deleting a device")
    public void whenValidDevice_deleteDevice_noContent() throws Exception {
        mockMvc.perform(delete("/api/devices/remove/{id}", sampledModel.getDeviceBaseId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
