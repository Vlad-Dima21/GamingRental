package com.vladima.gamingrental.integration.controllers;

import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.repositories.GameCopyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedHashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameCopyControllerMYSQLTests extends BaseControllerMYSQLTests<DeviceBase>{

    @Autowired
    private DeviceBaseRepository repository;

    @Override
    protected JpaRepository<DeviceBase, Long> getRepository() {
        return repository;
    }

    @Test
    @DisplayName("Integration test for fetching game copies based on the device and availability")
    public void whenFiltered_getGameCopies_returnCopiesJSON() throws Exception {
        var deviceBaseId = sampledModel.getDeviceBaseId();
        var stringResult = mockMvc.perform(get("/api/games")
                        .param("deviceId", String.valueOf(deviceBaseId))
                        .param("onlyAvailable", String.valueOf(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn().getResponse().getContentAsString();
        var result = new JacksonJsonParser().parseList(stringResult);
        if (!result.isEmpty()) {
            var resultDeviceName = ((LinkedHashMap<?,?>)((LinkedHashMap<?, ?>) result.get(0)).get("gameDevice")).get("deviceBaseName").toString();
            assertEquals(sampledModel.getDeviceBaseName(), resultDeviceName);
        }
    }
}
