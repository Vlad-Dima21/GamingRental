package com.vladima.gamingrental.unit.services;

import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.repositories.RentalRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.client.services.implementations.RentalServiceImpl;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.services.DeviceService;
import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.services.GameCopyService;
import com.vladima.gamingrental.helpers.EntityOperationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTests {
    @Mock
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DeviceService deviceService;

    @Mock
    private GameCopyService gameCopyService;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private Client client;
    private Game game;
    private DeviceBase deviceBase;

    @BeforeEach
    public void init() {
        client = new Client(1L, "ionut", "ion@email.com", "0729289182", null, new ArrayList<>());
        game = new Game(1L, "Roblox", "Sandbox", List.of());
        deviceBase = new DeviceBase(
                1L, "PS5", "Sony", 2018, null,
                null,
                null
        );
        deviceBase.setDevices(List.of(new Device(1L, 1, true, deviceBase, null)));
        deviceBase.setDeviceGameCopies(List.of(new GameCopy(1L, true, game, deviceBase)));
    }

    @Test
    @DisplayName("Unit test for fetching rentals by a client with an invalid email that throws an error")
    public void whenInvalidEmail_getRentals_throwsEntityOperationException() {
        var serviceException = new EntityOperationException(
            "Client not found",
            MessageFormat.format("Error fetching client with email {0}", client.getClientEmail()),
            HttpStatus.NOT_FOUND);
        given(clientService.getByEmail(client.getClientEmail()))
                .willThrow(serviceException);

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> rentalService.getRentals(client.getClientEmail(), deviceBase.getDeviceBaseName(), null, false, null, null));
        assertEquals(serviceException.getExtraInfo(), exception.getExtraInfo());
    }

    @Test
    @DisplayName("Unit test for fetching rentals with an nonexistent device name that throws an error")
    public void whenInvalidDeviceName_getRentals_throwsEntityOperationException() {
        given(deviceService.getByDeviceBaseName(deviceBase.getDeviceBaseName(), false))
                .willReturn(List.of());

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> rentalService.getRentals(client.getClientEmail(), deviceBase.getDeviceBaseName(), null, false, null, null));
        assertEquals(MessageFormat.format("No such device as {0}", deviceBase.getDeviceBaseName()), exception.getExtraInfo());
    }

//    @Test
//    @DisplayName("Unit test for creating a rental with an invalid client id that throws an error")
//    public void whenInvalidClientId_createRequest_throwsEntityOperationException() {
//        var serviceException = new EntityOperationException(
//            "Client not found",
//            MessageFormat.format("Error fetching client with id {0}", client.getClientEmail()),
//            HttpStatus.NOT_FOUND);
//        given(clientService.getModelById(client.getClientId()))
//                .willThrow(serviceException);
//
//        var exception = Assertions.assertThrows(EntityOperationException.class,
//                () -> rentalService.createRental(client.getClientEmail(), new RentalRequestDTO(deviceBase.getDevices().get(0).getDeviceId(),
//                        deviceBase.getDeviceGameCopies().stream().map(GameCopy::getGameCopyId).toList(),
//                        30L
//                    )));
//        assertEquals(serviceException.getExtraInfo(), exception.getExtraInfo());
//    }

    @Test
    @DisplayName("Unit test for creating a rental with an invalid device unit id that throws an error")
    public void whenInvalidDeviceId_createRequest_throwsEntityOperationException() {
        var device = deviceBase.getDevices().get(0);
        var serviceException = new EntityOperationException(
            "Device not found",
            MessageFormat.format("Error fetching device with id {0}", device.getDeviceId()),
            HttpStatus.NOT_FOUND);
        given(deviceService.getModelById(device.getDeviceId(), true))
                .willThrow(serviceException);

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> rentalService.createRental(
                        client.getClientEmail(), new RentalRequestDTO(device.getDeviceId(),
                        deviceBase.getDeviceGameCopies().stream().map(GameCopy::getGameCopyId).toList(),
                        30L
                )));
        assertEquals(serviceException.getExtraInfo(), exception.getExtraInfo());
    }

    @Test
    @DisplayName("Unit test for creating a rental with invalid game copies ids that throws an error")
    public void whenInvalidGameCopiesIds_createRequest_throwsEntityOperationException() {
        var device = deviceBase.getDevices().get(0);
        given(gameCopyService.getAvailableCopiesForDeviceByIds(ArgumentMatchers.anyList(), ArgumentMatchers.anyLong()))
                .willReturn(List.of());
        given(deviceService.getModelById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                .willReturn(device);

        var exception = Assertions.assertThrows(EntityOperationException.class,
                () -> rentalService.createRental(
                        client.getClientEmail(), new RentalRequestDTO(device.getDeviceId(),
                        deviceBase.getDeviceGameCopies().stream().map(GameCopy::getGameCopyId).toList(),
                        30L
                )));
        assertEquals("Invalid game copies", exception.getMessage());
    }

    @Test
    @DisplayName("Unit test for marking a nonexistent rental as returned")
    public void whenRentalDoesNotExist_rentalReturned_throwsEntityOperationException() {
        given(rentalRepository.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.empty());

        var exception = assertThrows(EntityOperationException.class,
                () -> rentalService.rentalReturned(1L));
        assertEquals(exception.getMessage(), "Rental not found");
    }

    @Test
    @DisplayName("Unit test for marking a rental as returned twice")
    public void whenRentalIsAlreadyReturned_rentalReturned_throwsEntityOperationException() {
        given(rentalRepository.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.of(new Rental(null, LocalDateTime.MIN, client, deviceBase.getDevices().get(0), game.getGameCopies())));

        var exception = assertThrows(EntityOperationException.class,
                () -> rentalService.rentalReturned(1L));
        assertEquals(exception.getMessage(), "Rental already returned");
    }
}
