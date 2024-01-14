package com.vladima.gamingrental.services;

import com.vladima.gamingrental.device.services.DeviceBaseService;
import com.vladima.gamingrental.games.repositories.GameCopyRepository;
import com.vladima.gamingrental.games.repositories.GameRepository;
import com.vladima.gamingrental.games.services.GameCopyServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class GameCopyServiceTests {
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameCopyServiceImpl service;

    @Test
    @DisplayName("Unit test for fetching the game copies for a specific game (by id) that throws an error")
    public void givenInvalidGameId_getCopies_throwsEntityOperationException() {
        var serviceException = new EntityOperationException(
            "Incorrect game ID",
            "",
            HttpStatus.NOT_FOUND
        );
        given(gameRepository.existsById(ArgumentMatchers.anyLong()))
                .willThrow(serviceException);
        var exception = assertThrows(EntityOperationException.class,
                () -> service.getCopies(1L, 1L, false));

        assertEquals(serviceException, exception);
    }
}
