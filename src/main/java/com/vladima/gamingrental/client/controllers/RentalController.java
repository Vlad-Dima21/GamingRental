package com.vladima.gamingrental.client.controllers;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.services.RentalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getLoans(
        @RequestParam(required = false) String clientEmail,
        @RequestParam(required = false) String  deviceName,
        @RequestParam(required = false) Boolean returned,
        @RequestParam(defaultValue = "false") boolean pastDue
    ) {
        return new ResponseEntity<>(rentalService.getRentals(clientEmail, deviceName, returned, pastDue), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<RentalDTO> createRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO) {
        return new ResponseEntity<>(rentalService.createRequest(rentalRequestDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/return/{id}")
    public ResponseEntity<RentalDTO> returnRental(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(rentalService.rentalReturned(id), HttpStatus.OK);
    }
}
