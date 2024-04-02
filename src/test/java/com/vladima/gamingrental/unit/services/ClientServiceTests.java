package com.vladima.gamingrental.unit.services;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.services.implementations.ClientServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    @DisplayName("Unit test for finding a client by id that returns the client")
    public void whenExistingClient_getById_returnsClient() {
        var client = new Client(1L, "Alexandru", "alex@email.com", "0720 000 000", null, new ArrayList<>());

        given(clientRepository.findById(client.getClientId()))
                .willReturn(Optional.of(client));

        var fetchedClient = clientService.getById(client.getClientId());
        assertEquals(fetchedClient.getClientEmail(), client.getClientEmail());
    }

    @Test
    @DisplayName("Unit test for finding a client by an invalid id that throws an error")
    public void whenNoClientWithId_getById_throwsEntityOperationException() {
        var invalidId = Long.MAX_VALUE;
        given(clientRepository.findById(invalidId))
                .willReturn(Optional.empty());

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> clientService.getById(invalidId));
        assertEquals(MessageFormat.format("Error fetching client with id {0}", invalidId), exception.getExtraInfo());
    }

    @Test
    @DisplayName("Unit test for finding a client by an nonexistent email that throws an error")
    public void whenInvalidEmail_getByEmail_throwsEntityOperationException() {
        String invalidEmail = "test";
        given(clientRepository.findByClientEmail(invalidEmail))
                .willReturn(null);

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> clientService.getByEmail(invalidEmail));
        assertEquals(MessageFormat.format("Error fetching client with email {0}", invalidEmail), exception.getExtraInfo());
    }

    @Test
    @DisplayName("Unit test for creating a client with an email already in use that throws an error")
    public void whenNewClientHasExistingEmail_create_throwsEntityOperationException() {
        var client = new Client(1L, "Alexandru", "alex@email.com", "0720 000 000", null, new ArrayList<>());
        given(clientRepository.findByClientEmail(client.getClientEmail()))
                .willReturn(client);

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> clientService.create(client.toDTO()));
        assertEquals("Email is already in use", exception.getExtraInfo());
    }
}
