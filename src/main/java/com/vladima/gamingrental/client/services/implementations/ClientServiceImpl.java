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
import java.util.Objects;

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
        var existingClientEmail = getRepository().findByClientEmail(clientDTO.getClientEmail());
        var existingClientPhone = getRepository().findByClientPhone(clientDTO.getClientPhone());
        if (existingClientEmail != null) {
            throw new EntityOperationException(
                "Client not registered",
                "Email is already in use",
                HttpStatus.CONFLICT
            );
        }
        if (existingClientPhone != null) {
            throw new EntityOperationException(
                    "Client not registered",
                    "Phone is already in use",
                    HttpStatus.CONFLICT
            );
        }
        return getRepository().save(clientDTO.toModel()).toDTO();
    }

    @Override
    @Transactional
    public ClientDTO updateInfo(Long id, ClientDTO clientDTO) {
        var client = getModelById(id);
        var cEmail = getRepository().findByClientEmail(clientDTO.getClientEmail());
        var cPhone = getRepository().findByClientPhone(clientDTO.getClientPhone());
        if (cPhone != null && !Objects.equals(cPhone.getClientId(), client.getClientId())) {
            throw new EntityOperationException(
                    "Client not updated",
                    "Phone is already in use",
                    HttpStatus.CONFLICT
            );
        }
        if (cEmail != null && !Objects.equals(cEmail.getClientId(), client.getClientId())) {
            throw new EntityOperationException(
                    "Client not updated",
                    "Email is already in use",
                    HttpStatus.CONFLICT
            );
        }
        client.setClientName(clientDTO.getClientName());
        client.setClientEmail(clientDTO.getClientEmail());
        client.setClientPhone(clientDTO.getClientPhone());
        return client.toDTO();
    }

    @Override
    public String getLogName() {
        return "Client";
    }
}
