package com.vladima.gamingrental.security.controllers;

import com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler;
import com.vladima.gamingrental.security.dto.UserClientDTO;
import com.vladima.gamingrental.security.dto.UserDTO;
import com.vladima.gamingrental.security.dto.UserResponseDTO;
import com.vladima.gamingrental.security.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
                    ),
                    description = "User logged in"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "User not found"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.login(userDTO), HttpStatus.OK);
    }

    @Operation(summary = "Register client user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))
                    ),
                    description = "Client registered"
            ),
            @ApiResponse(
                    responseCode = "409",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "Email or phone is already in use"
            ),
            @ApiResponse(
                    responseCode = "500",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EntitiesExceptionHandler.ExceptionFormat.class)
                    ),
                    description = "(Internal) Client role doesn't exist"
            )
    })
    @PostMapping("/register/client")
    public ResponseEntity<UserResponseDTO> registerClient(@Valid @RequestBody UserClientDTO userClientDTO) {
        return new ResponseEntity<>(userService.registerClient(userClientDTO), HttpStatus.OK);
    }
}
