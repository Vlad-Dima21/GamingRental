package com.vladima.gamingrental.client.repositories;

import com.vladima.gamingrental.client.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientEmail(String email);
    Client findByClientPhone(String phone);
    List<Client> findByClientNameContaining(String name);
    Client findByClientName(String name);
}
