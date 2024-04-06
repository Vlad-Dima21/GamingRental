package com.vladima.gamingrental.integration.controllers;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
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

import java.text.MessageFormat;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ClientControllerMYSQLTests extends BaseControllerMYSQLTests<Client> {

    @Autowired
    private ClientRepository repository;

    @Override
    protected ClientRepository getRepository() {
        return repository;
    }

    public void init() throws Exception {
        super.init();
        retrieveAdminToken(new AdminDTO("admin", "admin"));
    }

    @Test
    @DisplayName("Integration test for fetching client by id that returns the client")
    public void whenValidId_getClientById_returnsClient() throws Exception {
        mockMvc.perform(get("/api/clients/{id}", sampledModel.getClientId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value(sampledModel.getClientName()))
                .andExpect(jsonPath("$.clientEmail").value(sampledModel.getClientEmail()));
    }

    @Test
    @DisplayName("Integration test for fetching a client by id that returns an error with 404 status code")
    public void whenInvalidId_getClientById_returnsErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client not found",
                MessageFormat.format("Error fetching client with id {0}", Long.MAX_VALUE),
                HttpStatus.NOT_FOUND
        );
        mockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.details").value(exception.getExtraInfo()));
    }

    @Test
    @DisplayName("Integration test for creating a client with an email already in use that returns an error with 409 status code")
    public void whenExistingClientEmail_create_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client not registered",
                "Email is already in use",
                HttpStatus.CONFLICT
        );
        mockMvc.perform(post("/api/clients/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringifyJSON.toJSON(sampledModel.toDTO()))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(exception.getMessage()))
                .andExpect(jsonPath("$.details").value(exception.getExtraInfo()));
    }

    @Test
    @DisplayName("Integration test for creating a client with missing information that returns an error with 400 status code")
    public void whenMissingPhoneNumber_create_returnErrorJSON() throws Exception {
        mockMvc.perform(post("/api/clients/create")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringifyJSON.toJSON(new Client(sampledModel.getClientName(), sampledModel.getClientEmail(), null).toDTO())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Client must have a phone number"));
    }

    @Test
    @DisplayName("Integration test for filtering clients by name that returns a list of clients")
    public void whenNameFiltered_getFilteredClients_returnClientsJSON() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .param("name", sampledModel.getClientName())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientEmail").value(sampledModel.getClientEmail()));
    }

    @Test
    @DisplayName("Integration test for filtering clients by email that does not exist that returns error json")
    public void whenInvalidEmail_getFilteredClients_returnErrorJSON() throws Exception {
        var invalidEmail = "test";
        var exception = new EntityOperationException(
                "Client not found",
                MessageFormat.format("Error fetching client with email {0}", invalidEmail),
                HttpStatus.NOT_FOUND
        );
        mockMvc.perform(get("/api/clients")
                        .param("email", invalidEmail)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    @DisplayName("Integration test for updating a client's info that returns the client back")
    public void givenClient_updateClient_returnsClient() throws Exception {
        var modifiedClient = sampledModel.toDTO();
        modifiedClient.setClientPhone("0720 234 560");
        mockMvc.perform(put("/api/clients/update/{id}", sampledModel.getClientId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringifyJSON.toJSON(modifiedClient)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientEmail").value(sampledModel.getClientEmail()))
                .andExpect(jsonPath("$.clientPhone").value(modifiedClient.getClientPhone()));
    }

    @Test
    @DisplayName("Integration test for removing a client that does not exist that returns an error json")
    public void whenNonexistentClientId_deleteClient_returnErrorJSON() throws Exception {
        var exception = new EntityOperationException(
                "Client does not exist",
                MessageFormat.format("Client with id {0} does not exist", Long.MAX_VALUE),
                HttpStatus.NOT_FOUND
        );
        mockMvc.perform(delete("/api/clients/remove/{id}", Long.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }
}
