package com.vladima.gamingrental.integration.repositories;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.client.repositories.RentalRepository;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.games.models.GameCopy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RentalRepositoryTests {

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RentalRepository rentalRepository;

    private Rental rental;

    @BeforeEach
    public void init() {
        if (rental != null) {
            return;
        }
        rental = new Rental();
        var client = new Client("ionut", "ion@email.com", "0729289182", null, new ArrayList<>());
        var game = new Game("Roblox", "Sandbox", List.of());
        var device = new Device(3, true, null, List.of());
        var deviceBase = new DeviceBase(
                "PS5", "Sony", 2018, "",
                List.of(device),
                List.of()
        );
        device.setDeviceBase(deviceBase);
        deviceBase.setDevices(List.of(new Device(1, true, deviceBase, null)));
        deviceBase.setDeviceGameCopies(List.of(new GameCopy(true, game, deviceBase, List.of())));
        rental.setRentalDueDate(LocalDateTime.now().plusDays(30));
        rental.setRentalDevice(deviceBase.getDevices().get(0));
        rental.setRentalClient(client);
        device.setDeviceRentals(List.of(rental));

        entityManager.persistAndFlush(client);
        entityManager.persistAndFlush(game);
        entityManager.persistAndFlush(deviceBase);
        entityManager.persistAndFlush(device);
        entityManager.persistAndFlush(rental);
    }

    @Test
    public void getRentals_returnsRentals() {
        Page<Rental> rentals = rentalRepository.getRentals(
                rental.getRentalClient().getClientEmail(),
                rental.getRentalDevice().getDeviceBase().getDeviceBaseName(),
                false,
                false,
                PageRequest.of(0, 100)
        );
        assertThat(StreamSupport.stream(rentals.spliterator(), false)).contains(rental);
    }

    @Test
    public void getRentals_returnsEmptyWhenNotFound() {
        Page<Rental> rentals = rentalRepository.getRentals(
                "Nonexistent Email",
                "Nonexistent Device",
                true,
                true,
                PageRequest.of(0, 10)
        );
        assertThat(StreamSupport.stream(rentals.spliterator(), false)).doesNotContain(rental);
    }
}
