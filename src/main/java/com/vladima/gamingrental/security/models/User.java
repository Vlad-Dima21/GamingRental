package com.vladima.gamingrental.security.models;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.helpers.BaseModel;
import com.vladima.gamingrental.security.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class User implements UserDetails, BaseModel<UserDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userPassword;

    @OneToOne(mappedBy = "clientUser", optional = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_role_id")
    private Role authority;

    public User(String userPassword, String userEmail) {
        this.userPassword = userPassword;
        this.client = new Client(null, userEmail, null);
    }


    @Override
    public Set<Role> getAuthorities() {
        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return client.getClientEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO(getUsername(), getPassword());
    }
}
