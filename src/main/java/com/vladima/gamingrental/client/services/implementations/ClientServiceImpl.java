package com.vladima.gamingrental.client.services.implementations;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
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

    private final int PAGE_SIZE = 10;

    @Override
    public PageableResponseDTO<ClientDTO> getFiltered(String email, String name, Integer page, SortDirection sort) {
        var pageRequest = PageRequest.of(page != null ? page - 1 : 0, PAGE_SIZE);
        pageRequest = sort != null ? pageRequest.withSort(sort.by("clientName")): pageRequest;
        if (email == null && name == null) {
            return getAllPageable(pageRequest);
        }
        if (email != null) {
            return new PageableResponseDTO<>(1, List.of(getByEmail(email)));
        }
        return getByName(name, pageRequest);
    }

    public PageableResponseDTO<ClientDTO> getAllPageable(PageRequest pageRequest) {
        var result =  getRepository().findAll(pageRequest);
        var items = result.stream().map(Client::toDTO).toList();
        var total = result.getTotalPages();
        return new PageableResponseDTO<>(total, items);
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
    public PageableResponseDTO<ClientDTO> getByName(String name, PageRequest pageRequest) {
        var result = getRepository().findByClientNameContaining(name, pageRequest);
        var items = result.stream()
                .map(Client::toDTO)
                .toList();
        return new PageableResponseDTO<>(result.getTotalPages(), items);
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
