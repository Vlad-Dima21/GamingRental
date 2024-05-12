package com.vladima.gamingrental.unit.repositories;

import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.repositories.GameCopyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class GameCopyRepositoryTests {

    @TestConfiguration
    static class Configuration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameCopyRepository gameCopyRepository;

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game("Roblox", "Sandbox", List.of());
        DeviceBase deviceBase = new DeviceBase("PS5", "Sony", 2021, "", List.of(), List.of());
        GameCopy gameCopy = new GameCopy(true, game, deviceBase, List.of());
        game.setGameCopies(List.of(gameCopy));

        entityManager.persistAndFlush(deviceBase);
        entityManager.persistAndFlush(game);
        entityManager.persistAndFlush(gameCopy);
    }

    @Test
    public void findGameCopyByGameTitle_returnsGameCopy() {
        var foundGames = gameCopyRepository.findByGameName(game.getGameName()).size();
        assertEquals(1, foundGames);
    }

    @Test
    public void findGameCopyByGameTitle_returnsNullWhenNotFound() {
        var foundGames = gameCopyRepository.findByGameName("Nonexistent Game").size();
        assertEquals(0, foundGames);
    }
}