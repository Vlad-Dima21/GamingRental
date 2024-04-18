package com.vladima.gamingrental.integration.services;

import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.repositories.RentalRepository;
import com.vladima.gamingrental.client.services.RentalService;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.repositories.DeviceRepository;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("mysql")
@Transactional
public class RentalServiceMYSQLTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RentalService rentalService;

    private Client sampledClient;

    @BeforeEach
    public void init() {
        var random = new Random();
        var clients = clientRepository.findAll();
        sampledClient = clients.get(random.nextInt(clients.size()));
    }

    @Test
    @DisplayName("Integration test for fetching rentals by a client with an invalid email that throws an error")
    public void whenInvalidEmail_getRentals_throwsEntityOperationException() {
        var invalidEmail = "email_not_in_use@email.com";
        var expected = new EntityOperationException(
            "Client not found",
            MessageFormat.format("Error fetching client with email {0}", invalidEmail),
            HttpStatus.NOT_FOUND
        );
        var exception = assertThrows(EntityOperationException.class,
                () -> rentalService.getRentals(invalidEmail, "", false, false, null, null)
        );
        assertEquals(expected, exception);
    }

    @Test
    @DisplayName("Integration test for fetching rentals with an nonexistent device name that throws an error")
    public void whenInvalidDeviceName_getRentals_throwsEntityOperationException() {
        var invalidDevice = "Spaghetti";
        var exception = assertThrows(EntityOperationException.class,
                () -> rentalService.getRentals(sampledClient.getClientEmail(), invalidDevice, false, false, null, null)
        );
        assertEquals(MessageFormat.format("No such device as {0}", invalidDevice), exception.getExtraInfo());
    }

    @Test
    @DisplayName("Integration test for creating a rental with an invalid device unit id that throws an error")
    public void whenInvalidDeviceId_createRequest_throwsEntityOperationException() {
        var invalidId = Long.MAX_VALUE;
        var expected = new EntityOperationException(
                "Unit not found",
                MessageFormat.format("Error fetching unit with id {0}", invalidId),
                HttpStatus.NOT_FOUND);
        var exception = assertThrows(EntityOperationException.class,
                () -> rentalService.createRental(sampledClient.getClientEmail(), new RentalRequestDTO(invalidId, null, 30L)));
        assertEquals(expected, exception);
    }

    @Test
    @DisplayName("Integration test for creating a rental with invalid game copies ids that throws an error")
    public void whenInvalidGameCopiesIds_createRequest_throwsEntityOperationException() {
        var expected = new EntityOperationException(
                "Invalid game copies",
                "Copies may not exist or be unavailable",
                HttpStatus.BAD_REQUEST
        );
        var exception = assertThrows(EntityOperationException.class,
                () -> rentalService.createRental(sampledClient.getClientEmail(),
                new RentalRequestDTO(1L, List.of(Long.MAX_VALUE), 30L)));
        assertEquals(expected, exception);
    }

    @Test
    @DisplayName("Integration test for marking a nonexistent rental as returned")
    public void whenRentalDoesNotExist_rentalReturned_throwsEntityOperationException() {
        assertEquals("Rental not found", assertThrows(EntityOperationException.class, () -> rentalService.rentalReturned(Long.MAX_VALUE)).getMessage());
    }
}
