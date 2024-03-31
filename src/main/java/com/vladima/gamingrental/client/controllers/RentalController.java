package com.vladima.gamingrental.client.controllers;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.services.RentalService;
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

import javax.lang.model.element.Name;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @Operation(summary = "Get filtered rentals")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RentalDTO.class))
                    ),
                    description = "Valid filters and rentals found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Client or device not found"
            )
    })
    @GetMapping
    public ResponseEntity<List<RentalDTO>> getRentals(
        Authentication authentication,
//        @RequestParam(required = false) @Parameter(description = "Client Email") String clientEmail,
        @RequestParam(required = false) @Parameter(description = "Device Name") String  deviceName,
        @RequestParam(required = false) @Parameter(description = "Rental is returned") Boolean returned,
        @RequestParam(defaultValue = "false") @Parameter(description = "Only rentals that are past due") boolean pastDue
    ) {
        return new ResponseEntity<>(rentalService.getRentals(authentication.getName(), deviceName, returned, pastDue), HttpStatus.OK);
    }

    @Operation(summary = "Register a new rental")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RentalDTO.class)
                    ),
                    description = "Rental was registered successfully"
            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
//                    ),
//                    description = "Client not found or device unit not found"
//            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Device unit not available or game copies not found/not available"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<RentalDTO> createRental(
            Authentication authentication,
            @Valid @RequestBody RentalRequestDTO rentalRequestDTO
    ) {
        return new ResponseEntity<>(rentalService.createRental(authentication.getName(), rentalRequestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Register rental return")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RentalDTO.class)
                    ),
                    description = "Rental was returned successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Rental was not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Rental has been already returned"
            )
    })
    @PatchMapping("/return/{id}")
    public ResponseEntity<RentalDTO> returnRental(@PathVariable @Min(1) @Parameter(description = "Rental ID") Long id) {
        return new ResponseEntity<>(rentalService.rentalReturned(id), HttpStatus.OK);
    }
}
