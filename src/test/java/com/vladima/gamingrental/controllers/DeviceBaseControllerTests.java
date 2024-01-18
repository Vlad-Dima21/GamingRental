package com.vladima.gamingrental.controllers;

import com.vladima.gamingrental.device.controllers.DeviceBaseController;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.services.DeviceBaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.helpers.StringifyJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DeviceBaseController.class)
public class DeviceBaseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceBaseServiceImpl service;

    private DeviceBase ps5;

    @BeforeEach
    public void init() {
        ps5 = new DeviceBase(
                1L, "PS5", "Sony",
                2018, List.of(new Device(2, true)),
                List.of()
        );
    }

    @Test
    @DisplayName("Unit test for creating a new device with an already used name that returns an error json")
    public void  whenExistingDeviceName_createDevice_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Device not added",
                "A device with the same name already exists",
                HttpStatus.CONFLICT
        );
        given(service.create(ArgumentMatchers.any()))
                .willThrow(exception);
        mockMvc.perform(post("/api/devices/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringifyJSON.toJSON(ps5.toDTO())))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @DisplayName("Unit test for fetching devices given a filter")
    public void whenFilteredByName_getFilteredDevices_returnDevicesJSON() throws Exception {
        var searchBy = ps5.getDeviceBaseName().substring(0,2);
        given(service.getFiltered(searchBy, null, null, false))
                .willReturn(List.of(ps5.toDTO()));
        mockMvc.perform(get("/api/devices")
                .param("name", searchBy))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deviceBaseName").value(ps5.getDeviceBaseName()));
    }

    @Test
    @DisplayName("Unit test for deleting a device")
    public void whenValidDevice_deleteDevice_noContent() throws Exception {
        mockMvc.perform(delete("/api/devices/remove/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
