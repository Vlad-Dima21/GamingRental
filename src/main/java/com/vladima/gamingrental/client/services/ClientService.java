package com.vladima.gamingrental.client.services;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.helpers.BaseService;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ClientService extends BaseService<Client, ClientDTO> {
    ClientDTO getByEmail(String email);
    PageableResponseDTO<ClientDTO> getByName(String name, PageRequest pageRequest);

    PageableResponseDTO<ClientDTO> getFiltered(String email, String name, Integer page, SortDirection sort);
}
