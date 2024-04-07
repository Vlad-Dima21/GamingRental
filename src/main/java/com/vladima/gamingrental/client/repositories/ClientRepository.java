package com.vladima.gamingrental.client.repositories;

import com.vladima.gamingrental.client.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientEmail(String email);
    Client findByClientPhone(String phone);
    Page<Client> findByClientNameContaining(String name, Pageable pageable);
    Client findByClientName(String name);
}
