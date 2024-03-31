package com.vladima.gamingrental.security.repositories;

import com.vladima.gamingrental.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClient_ClientEmail(String client_clientEmail);
}
