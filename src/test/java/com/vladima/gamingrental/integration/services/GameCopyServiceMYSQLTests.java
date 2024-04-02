package com.vladima.gamingrental.integration.services;


import com.vladima.gamingrental.games.services.GameCopyService;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("mysql")
@Transactional
public class GameCopyServiceMYSQLTests {

    @Autowired
    private GameCopyService service;

    @Test
    @DisplayName("Integration test for fetching the game copies for a specific game (by id) that throws an error")
    public void givenInvalidGameId_getCopies_throwsEntityOperationException() {
        var maxId = Long.MAX_VALUE;
        var expected = new EntityOperationException(
                "Incorrect game ID",
                "",
                HttpStatus.NOT_FOUND
        );
        var exception = assertThrows(EntityOperationException.class,
                () -> service.getCopies(maxId, maxId, false));
        assertEquals(expected, exception);
    }
}
