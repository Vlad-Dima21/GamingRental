package com.vladima.gamingrental.integration.controllers;

import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.repositories.DeviceRepository;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.security.dto.UserClientDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeviceControllerMYSQLTests extends BaseControllerMYSQLTests<Device>{

    @Autowired
    private DeviceRepository repository;

    @Override
    protected DeviceRepository getRepository() {
        return repository;
    }

    @Test
    @DisplayName("Integration test for fetching units based on the device id")
    public void whenInvalidId_getByDeviceBaseId_returnErrorJSON() throws Exception {
        var invalidId = Long.MAX_VALUE;
        var expected = new EntityOperationException(
                "Device not found",
                "Error fetching device with id " + invalidId,
                HttpStatus.NOT_FOUND
        );
        mockMvc.perform(get("/api/units/of-id/{id}", invalidId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expected.getMessage()));
    }

    @Test
    @DisplayName("Integration test for fetching units that are available")
    public void whenFilteredByAvailability_getByDeviceBaseName_returnUnitsJSON() throws Exception {
        var deviceBaseName = sampledModel.getDeviceBase().getDeviceBaseName();
        var stringResult = mockMvc.perform(get("/api/units/of")
                        .param("name", deviceBaseName)
                        .param("availableOnly", String.valueOf(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();
        var result = new JacksonJsonParser().parseList(stringResult)
                .stream().filter(d -> ((LinkedHashMap<?, ?>)d).containsValue("deviceIsAvailable")).toList().size();
        assertEquals(0, result);
    }
}
