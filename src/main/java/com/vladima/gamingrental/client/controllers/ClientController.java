package com.vladima.gamingrental.client.controllers;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.services.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable @Min(0) Long id) {
        return new ResponseEntity<>(clientService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getFilteredClients(@RequestParam(required = false) String email, @RequestParam(required = false) String name) {
        if (email == null && name == null) {
            return new ResponseEntity<>(clientService.getAll(), HttpStatus.OK);
        }
        if (email != null) {
            return new ResponseEntity<>(List.of(clientService.getByEmail(email)), HttpStatus.OK);
        }
        return new ResponseEntity<>(clientService.getByName(name), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.create(clientDTO), HttpStatus.CREATED);
    }
}
