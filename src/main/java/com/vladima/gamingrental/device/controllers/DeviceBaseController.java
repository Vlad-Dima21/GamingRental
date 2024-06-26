package com.vladima.gamingrental.device.controllers;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.device.services.DeviceBaseService;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceBaseController {

    private final DeviceBaseService deviceBaseService;

    @Operation(summary = "Get device by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeviceBaseExtrasDTO.class)
                    ),
                    description = "Device found"
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
    @GetMapping("/{id}")
    public ResponseEntity<DeviceBaseExtrasDTO> getDeviceById(
        @PathVariable @Min(1) @Parameter(description = "Device ID") Long id
    ) {
        return new ResponseEntity<>(deviceBaseService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get filtered devices")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DeviceBaseExtrasDTO.class))
                    ),
                    description = "Valid filters"
            )
    })
    @GetMapping
    public ResponseEntity<PageableResponseDTO<DeviceBaseExtrasDTO>> getFilteredDevices(
            @RequestParam(required = false) @Parameter(description = "Name of device") String name,
            @RequestParam(required = false) @Parameter(description = "Producer name") String producer,
            @RequestParam(required = false) @Parameter(description = "Released after the year") Integer year,
            @RequestParam(required = false, defaultValue = "false") @Parameter(description = "Only available") boolean ifAvailable,
            @RequestParam(required = false, defaultValue = "1") @Parameter(description = "Page number") @Min(1) int page,
            @RequestParam(required = false, defaultValue = "asc") @Parameter(description = "Sort devices by name")SortDirection sort
            ) {
        return new ResponseEntity<>(deviceBaseService.getFiltered(name, producer, year, ifAvailable, page, sort), HttpStatus.OK);
    }

    @Operation(summary = "Register a new device")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DeviceBaseExtrasDTO.class)
                    ),
                    description = "Device was registered"
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Device with the same name already exists"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<DeviceBaseDTO> createDevice(@Valid @RequestBody DeviceBaseDTO deviceBaseDTO) {
        return new ResponseEntity<>(deviceBaseService.create(deviceBaseDTO.toExtended()), HttpStatus.OK);
    }

    @Operation(summary = "Remove a device")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Device was removed"
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
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteDevice(
            @PathVariable @Min(1) @Parameter(description = "Device ID") Long id
    ) {
        deviceBaseService.removeById(id);
        return new ResponseEntity<>("Device deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceBaseExtrasDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceBaseService.getAll());
    }
}
