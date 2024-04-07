package com.vladima.gamingrental.unit.controllers;

import com.vladima.gamingrental.client.controllers.RentalController;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.client.services.implementations.RentalServiceImpl;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.helpers.StringifyJSON;
import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import com.vladima.gamingrental.unit.configurations.TestConfigurationSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RentalController.class)
@ContextConfiguration(classes = RentalController.class)
@Import({TestConfigurationSecurity.class, EntitiesExceptionHandler.class})
public class RentalControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalServiceImpl service;

    private Client client;
    private Game game;
    private DeviceBase deviceBase;

    @BeforeEach
    public void init() {
        client = new Client(1L, "ionut", "ion@email.com", "0729289182", null, List.of());
        game = new Game(1L, "Roblox", "Sandbox", List.of());
        deviceBase = new DeviceBase(
                1L, "PS5", "Sony", 2018,
                null,
                null
        );
        deviceBase.setDevices(List.of(new Device(1L, 1, true, deviceBase, null)));
        deviceBase.setDeviceGameCopies(List.of(new GameCopy(1L, true, game, deviceBase)));
        client.setClientRentals(List.of(new Rental(LocalDateTime.now(), null, client, deviceBase.getDevices().get(0), deviceBase.getDeviceGameCopies())));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    @DisplayName("Unit test for fetching rentals with a nonexistent client email that returns an error")
    public void whenInvalidName_getRentals_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
            "Client not found",
            MessageFormat.format("Error fetching client with email {0}", client.getClientEmail()),
            HttpStatus.NOT_FOUND);
        given(service.getRentals("test",null,null,false, null, null))
                .willThrow(exception);

        mockMvc.perform(get("/api/rentals")
                .param("clientEmail", client.getClientEmail()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.details").value(exception.getExtraInfo()));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    @DisplayName("Unit test for fetching rentals that returns the list")
    public void givenFilters_getRentals_returnRentalsJSON() throws Exception {
        given(service.getRentals("test", deviceBase.getDeviceBaseName(), null, false, null, null))
                .willReturn(client.getClientRentals().stream().map(Rental::toDTO).toList());

        mockMvc.perform(get("/api/rentals")
                .param("deviceName", deviceBase.getDeviceBaseName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rentalClient.clientName").value(client.getClientName()));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    @DisplayName("Unit test for renting games that are not available that returns an error json")
    public void whenNoAvailableGameCopies_createRental_returnsErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Invalid game copies",
                "Copies may not exist or be unavailable",
                HttpStatus.BAD_REQUEST
        );
        given(service.createRental(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .willThrow(exception);
        mockMvc.perform(post("/api/rentals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringifyJSON.toJSON(
                    new RentalRequestDTO(deviceBase.getDeviceBaseId(),
                    deviceBase.getDeviceGameCopies().stream().map(GameCopy::getGameCopyId).toList(),
                    45L)
                )))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @DisplayName("Unit test for finishing a rental twice that returns an error json")
    public void whenRentalAlreadyReturned_returnRental_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Rental already returned",
                MessageFormat.format("Was returned on {0}", LocalDateTime.now().minusDays(30L)),
                HttpStatus.BAD_REQUEST
        );
        given(service.rentalReturned(ArgumentMatchers.anyLong()))
                .willThrow(exception);
        mockMvc.perform(patch("/api/rentals/return/{id}", 10L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }
}
