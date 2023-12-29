package com.vladima.gamingrental.client.services.implementations;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.exceptions.ClientException;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    @Override
    public List<Client> getAll() {
        return repository.findAll();
    }

    @Override
    public Client getById(Long id) {
        Client client = repository.findById(id).orElseThrow(() ->
                new ClientException(MessageFormat.format("clientId = {0}", id)));
        return client;
    }

    @Override
    public Client getByEmail(String email) {
        Client client = repository.findByClientEmail(email);
        if (client == null) {
            throw new ClientException(MessageFormat.format("clientEmail = {0}", email));
        }
        return client;
    }

    @Override
    public List<Client> getByName(String name) {
        return repository.findByClientNameContaining(name);
    }

    @Override
    public Client create(ClientDTO clientDTO) {
        return repository.save(clientDTO.toModel());
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
