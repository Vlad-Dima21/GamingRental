package com.vladima.gamingrental.client.services.implementations;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ClientServiceImpl extends BaseServiceImpl<Client, ClientDTO, ClientRepository> implements ClientService {

    public ClientServiceImpl(ClientRepository repository) {
        super(repository);
    }

    @Override
    public ClientDTO getByEmail(String email) {
        Client client = getRepository().findByClientEmail(email);
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
        return getRepository().findByClientNameContaining(name)
                .stream()
                .map(Client::toDTO)
                .toList();
    }

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        var existingClient = getRepository().findByClientEmail(clientDTO.getClientEmail());
        if (existingClient != null) {
            throw new EntityOperationException(
                "Client not registered",
                "Email is already in use",
                HttpStatus.CONFLICT
            );
        }
        return getRepository().save(clientDTO.toModel()).toDTO();
    }

    @Override
    @Transactional
    public ClientDTO updateInfo(Long id, ClientDTO clientDTO) {
        var client = getModelById(id);
        client.setClientName(clientDTO.getClientName());
        client.setClientEmail(clientDTO.getClientEmail());
        client.setClientPhone(client.getClientPhone());
        return client.toDTO();
    }

    @Override
    public String getLogName() {
        return "Client";
    }
}
