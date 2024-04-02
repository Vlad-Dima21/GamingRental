package com.vladima.gamingrental.unit.controllers;

import com.vladima.gamingrental.client.controllers.ClientController;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.services.implementations.ClientServiceImpl;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ClientController.class)
@WebMvcTest(ClientController.class)
@Import({TestConfigurationSecurity.class, EntitiesExceptionHandler.class})
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientServiceImpl service;

    private Client client1;
    private Client client2;

    @BeforeEach
    public void init() {
        client1 = new Client(1L, "Adrian", "adrian@email.com", "0720 000 000", null, List.of());
        client2 = new Client(2L, "Vasile", "vasile@email.com", "0720 000 000", null, List.of());
    }

    @Test
    @DisplayName("Unit test for fetching client by id that returns the client")
    public void whenValidId_getClientById_returnsClient() throws Exception {
        given(service.getById(ArgumentMatchers.anyLong()))
                .willReturn(client1.toDTO());

        mockMvc.perform(get("/api/clients/{id}", client1.getClientId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value(client1.getClientName()))
                .andExpect(jsonPath("$.clientEmail").value(client1.getClientEmail()));
    }

    @Test
    @DisplayName("Unit test for fetching a client by id that returns an error with 404 status code")
    public void whenInvalidId_getClientById_returnsErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client not found",
                MessageFormat.format("Error fetching client with id {0}", client1.getClientId()),
                HttpStatus.NOT_FOUND
        );
        given(service.getById(ArgumentMatchers.anyLong()))
                .willThrow(exception);

        mockMvc.perform(get("/api/clients/{id}", client1.getClientId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.details").value(exception.getExtraInfo()));
    }

    @Test
    @DisplayName("Unit test for creating a client with an email already in use that returns an error with 409 status code")
    public void whenExistingClientEmail_create_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client not registered",
                "Email is already in use",
                HttpStatus.CONFLICT
        );
        given(service.create(ArgumentMatchers.any()))
                .willThrow(exception);
        mockMvc.perform(post("/api/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringifyJSON.toJSON(client1.toDTO())))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.details").value(exception.getExtraInfo()));
    }

    @Test
    @DisplayName("Unit test for creating a client with missing information that returns an error with 400 status code")
    public void whenMissingPhoneNumber_create_returnErrorJSON() throws Exception {
        mockMvc.perform(post("/api/clients/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringifyJSON.toJSON(new Client(client1.getClientName(), client1.getClientEmail(), null).toDTO())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Client must have a phone number"));
    }

    @Test
    @DisplayName("Unit test for filtering clients by name that returns a list of clients")
    public void whenNameFiltered_getFilteredClients_returnClientsJSON() throws Exception {
        given(service.getByName(ArgumentMatchers.anyString()))
                .willReturn(List.of(client1.toDTO(), client2.toDTO()));

        mockMvc.perform(get("/api/clients")
                .param("name", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientEmail").value(client1.getClientEmail()))
                .andExpect(jsonPath("$[1].clientEmail").value(client2.getClientEmail()));
    }

    @Test
    @DisplayName("Unit test for filtering clients by email that does not exist that returns error json")
    public void whenInvalidEmail_getFilteredClients_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client not found",
                MessageFormat.format("Error fetching client with email {0}", client1.getClientEmail()),
                HttpStatus.NOT_FOUND
        );
        given(service.getByEmail(ArgumentMatchers.anyString()))
                .willThrow(exception);

        mockMvc.perform(get("/api/clients")
                .param("email", client1.getClientEmail()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @DisplayName("Unit test for updating a client's info that returns the client back")
    public void givenClient_updateClient_returnsClient() throws Exception {
        given(service.updateInfo(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .willReturn(client1.toDTO());
        mockMvc.perform(put("/api/clients/update/{id}", client1.getClientId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringifyJSON.toJSON(client1.toDTO())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientEmail").value(client1.getClientEmail()));
    }

    @Test
    @DisplayName("Unit test for removing a client that does not exist that returns an error json")
    public void whenNonexistentClientId_deleteClient_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client does not exist",
                MessageFormat.format("Client with id {0} does not exist", client1.getClientId()),
                HttpStatus.NOT_FOUND
        );
        doThrow(exception).when(service).removeById(ArgumentMatchers.anyLong());
        mockMvc.perform(delete("/api/clients/remove/{id}", client1.getClientId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }
}
