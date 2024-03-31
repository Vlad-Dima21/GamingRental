package com.vladima.gamingrental.security.repositories;

import com.vladima.gamingrental.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClient_ClientEmail(String userEmail);
    Optional<User> findByClient_ClientPhone(String userPhone);

    Optional<User> findByAdminUsername(String username);
}
