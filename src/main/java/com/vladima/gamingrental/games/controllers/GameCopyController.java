package com.vladima.gamingrental.games.controllers;

import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.services.GameCopyService;
import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Get filtered game copies")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GameCopyDTO.class))
                    ),
                    description = "List of game copies"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Game or device not found"
            )
    })
    @GetMapping
    public ResponseEntity<List<GameCopyDTO>> getGameCopies(
        @RequestParam(required = false) @Parameter(description = "Filter by game ID") Long gameId,
        @RequestParam(required = false) @Parameter(description = "Filter by device ID") Long deviceId,
        @RequestParam(required = false) @Parameter(description = "Fetch only available copies") boolean onlyAvailable
    ) {
        return new ResponseEntity<>(gameCopyService.getCopies(gameId, deviceId, onlyAvailable), HttpStatus.OK);
    }
}
