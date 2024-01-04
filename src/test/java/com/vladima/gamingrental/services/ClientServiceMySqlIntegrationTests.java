package com.vladima.gamingrental.services;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.MessageFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ClientServiceMySqlIntegrationTests {

    @Autowired
    private ClientRepository clientRepository;


    @Autowired
    private ClientService clientService;

    private List<Client> testingClients;

    @BeforeEach
    public void setup() {
        // required for removing dev data
        clientRepository.deleteAll();
        List<Client> init = List.of(
            new Client("alexandru", "alexandru@email.com", "0720111111"),
            new Client("ionut", "ionut@email.com", "0720111112"),
            new Client("andrei", "andrei@email.com", "0720111113"),
            new Client("vasile", "vasile@email.com", "0720111114")
        );
        testingClients = clientRepository.saveAll(init);
    }

    @Test
    @DisplayName("Unit test for finding a client by id")
    public void whenExistingClient_getById_returnsClient() {
        var firstId = testingClients.get(0).getClientId();
        var client = clientService.getById(firstId);
        assertEquals(client.getClientName(), "alexandru");
    }

    @Test
    @DisplayName("When the ID does not match with any entity, EntityOperationException should be thrown")
    public void whenNoClientWithEmail_getById_throwsEntityOperationException() {
        var invalidId = Long.MAX_VALUE;
        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> clientService.getById(invalidId));
        assertEquals(MessageFormat.format("Error fetching client with id {0}", invalidId), exception.getExtraInfo());
    }
}
