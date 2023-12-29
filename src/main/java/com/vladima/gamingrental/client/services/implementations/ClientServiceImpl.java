package com.vladima.gamingrental.client.services.implementations;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.helpers.EntityOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public List<ClientDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(Client::toDTO)
                .toList();
    }

    public Client getOriginalById(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new EntityOperationException(
                    "Client not found",
                    MessageFormat.format("Error fetching client with id {0}", id),
                    HttpStatus.NOT_FOUND
            )
        );
    }

    @Override
    public ClientDTO getById(Long id) {
        return getOriginalById(id).toDTO();
    }

    @Override
    public ClientDTO getByEmail(String email) {
        Client client = repository.findByClientEmail(email);
        if (client == null) {
            throw new EntityOperationException(
                "Client not found",
                MessageFormat.format("Error fetching client with email {0}", email),
                HttpStatus.NOT_FOUND
            );
        }
        return client.toDTO();
    }

    @Override
    public List<ClientDTO> getByName(String name) {
        return repository.findByClientNameContaining(name)
                .stream()
                .map(Client::toDTO)
                .toList();
    }

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        var existingClient = repository.findByClientEmail(clientDTO.getClientEmail());
        if (existingClient != null) {
            throw new EntityOperationException(
                "Client not registered",
                "Email is already in use",
                HttpStatus.BAD_REQUEST
            );
        }
        return repository.save(clientDTO.toModel()).toDTO();
    }

    @Override
    public ClientDTO updateInfo(Long id, ClientDTO clientDTO) {
        var client = getOriginalById(id);
        client.setClientName(clientDTO.getClientName());
        client.setClientEmail(clientDTO.getClientEmail());
        client.setClientPhone(client.getClientPhone());
        repository.save(client);
        return client.toDTO();
    }

    @Override
    public void removeById(Long id) {
        repository.findById(id).orElseThrow(() -> new EntityOperationException(
                "Client does not exist",
                MessageFormat.format("Client with id {0} does not exist", id),
                HttpStatus.BAD_REQUEST
        ));
        repository.deleteById(id);
    }
}
