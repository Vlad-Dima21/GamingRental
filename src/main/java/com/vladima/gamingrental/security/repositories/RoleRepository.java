package com.vladima.gamingrental.security.repositories;

import com.vladima.gamingrental.security.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(String authority);
}
