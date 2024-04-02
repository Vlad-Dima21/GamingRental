package com.vladima.gamingrental.unit.controllers;

import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.controllers.GameCopyController;
import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.services.GameCopyService;
import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import com.vladima.gamingrental.unit.configurations.TestConfigurationSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GameCopyController.class)
@ContextConfiguration(classes = GameCopyController.class)
@Import({TestConfigurationSecurity.class, EntitiesExceptionHandler.class})
public class GameCopyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameCopyService service;

    private DeviceBase ps5;

    @BeforeEach
    public void init() {
        ps5 = new DeviceBase("PS5", "Sony", 2018);
        ps5.setDeviceGameCopies(List.of(new GameCopy(new Game("Amongus", "Action"), ps5, true)));
        ps5.setDeviceBaseId(1L);
    }

    @Test
    @DisplayName("Unit test for fetching game copies based on the device and availability")
    public void whenFiltered_getGameCopies_returnCopiesJSON() throws Exception {
        given(service.getCopies(null, ps5.getDeviceBaseId(), true))
                .willReturn(ps5.getDeviceGameCopies().stream().map(GameCopy::toDTO).toList());
        mockMvc.perform(get("/api/games")
                .param("deviceId", String.valueOf(ps5.getDeviceBaseId()))
                .param("onlyAvailable", String.valueOf(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameDevice.deviceBaseName")
                        .value(ps5.getDeviceBaseName()));
    }
}
