package com.vladima.gamingrental.client.services;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAll();
    ClientDTO getById(Long id);
    ClientDTO getByEmail(String email);
    List<ClientDTO> getByName(String name);
    ClientDTO create(ClientDTO clientDTO);
    ClientDTO updateInfo(Long id, ClientDTO clientDTO);
    void removeById(Long id);
}
