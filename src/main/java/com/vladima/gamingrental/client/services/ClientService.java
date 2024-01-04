package com.vladima.gamingrental.client.services;

import com.vladima.gamingrental.client.dto.ClientDTO;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface ClientService extends BaseService<Client, ClientDTO> {
    ClientDTO getByEmail(String email);
    List<ClientDTO> getByName(String name);
}
