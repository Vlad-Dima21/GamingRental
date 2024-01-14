package com.vladima.gamingrental.device.controllers;

import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.device.dto.DeviceExtrasDTO;
import com.vladima.gamingrental.device.services.DeviceService;
import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Get device units by the device ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeviceBaseExtrasDTO.class)
                    ),
                    description = "Device units returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Device not found"
            )
    })
    @GetMapping("/of-id/{id}")
    public ResponseEntity<List<DeviceExtrasDTO>> getByDeviceBaseId(
            @PathVariable @Min(1) @Parameter(description = "Device ID") Long id
    ) {
        return new ResponseEntity<>(deviceService.getByDeviceBaseId(id), HttpStatus.OK);
    }

    @Operation(summary = "Get device units by the device and filter by availability")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeviceBaseExtrasDTO.class)
                    ),
                    description = "Device units returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Device not found"
            )
    })
    @GetMapping("/of")
    public ResponseEntity<List<DeviceExtrasDTO>> getByDeviceBaseName(
            @RequestParam @Parameter(description = "Device name") String name,
            @RequestParam(required = false, defaultValue = "false") boolean availableOnly
    ) {
        return new ResponseEntity<>(deviceService.getByDeviceBaseName(name, availableOnly), HttpStatus.OK);
    }
}
