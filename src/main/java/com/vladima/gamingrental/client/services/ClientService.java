package com.vladima.gamingrental.client.services;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;

import java.util.List;

public interface ClientService {

    List<Client> getAll();
    Client getById(Long id);
    Client getByEmail(String email);
    List<Client> getByName(String name);
    Client create(ClientDTO clientDTO);
    void removeById(Long id);
}
