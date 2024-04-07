package com.vladima.gamingrental.client.controllers;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.services.ClientService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Get a client by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientDTO.class)
                    ),
                    description = "Client found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Client not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable @Parameter(description = "The client ID") @Min(1) Long id) {
        return new ResponseEntity<>(clientService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Filter clients")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class))
                    ),
                    description = "Valid filters and clients found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Invalid filters"
            )
    })
    @GetMapping
    public ResponseEntity<PageableResponseDTO<ClientDTO>> getFilteredClients(
            @RequestParam(required = false) @Parameter(description = "Filtered by full email") String email,
            @RequestParam(required = false) @Parameter(description = "Filtered by name (may be contained)") String name,
            @RequestParam(required = false) @Parameter(description = "List page number") @Min(1) Integer page,
            @RequestParam(required = false) @Parameter(description = "Sort clients by name") SortDirection sort)
    {
        return ResponseEntity.ok(clientService.getFiltered(email, name, page, sort));
    }

    @Operation(summary = "Add a new client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class))
                    ),
                    description = "Client added"
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Email or phone is already in use"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.create(clientDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update client info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class))
                    ),
                    description = "Client info updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Client not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Email or phone is already in use"
            )
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<ClientDTO> updateClient(
        @PathVariable @Min(1) @Parameter(description = "The client ID") Long id,
        @Valid @RequestBody ClientDTO clientDTO)
    {
        return new ResponseEntity<>(clientService.updateInfo(id, clientDTO), HttpStatus.OK);
    }

    @Operation(summary = "Remove a client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Client removed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Client not found"
            )
    })
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteClient(
        @PathVariable @Min(1) @Parameter(description = "The client ID") Long id)
    {
        clientService.removeById(id);
        return new ResponseEntity<>("Client deleted", HttpStatus.NO_CONTENT);
    }
}
