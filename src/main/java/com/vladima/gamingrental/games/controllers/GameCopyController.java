package com.vladima.gamingrental.games.controllers;

import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.services.GameCopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameCopyController {
    private final GameCopyService gameCopyService;

    @GetMapping
    public ResponseEntity<List<GameCopyDTO>> getGameCopies(
        @RequestParam(required = false) Long gameId,
        @RequestParam(required = false) Long deviceId,
        @RequestParam(required = false) boolean onlyAvailable
    ) {
        return new ResponseEntity<>(gameCopyService.getCopies(gameId, deviceId, onlyAvailable), HttpStatus.OK);
    }
}
