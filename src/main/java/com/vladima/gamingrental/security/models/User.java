package com.vladima.gamingrental.security.models;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.helpers.BaseModel;
import com.vladima.gamingrental.security.dto.UserClientDTO;
import com.vladima.gamingrental.security.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
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

    @Column(unique = true)
    private String adminUsername;

    @OneToOne(mappedBy = "clientUser", optional = false, cascade = CascadeType.ALL)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_role_id")
    private Role authority;

    public User(String userPassword, String userEmail, String userName, String userPhone) {
        this.userPassword = userPassword;
        this.client = new Client(userName, userEmail, userPhone, this);
    }

    public User(String userEmail, String userPassword) {
        throw new UnsupportedOperationException("Only client users can be created using an email");
    }

    public User(String adminUsername, String userPassword, Role authority) {
        this.adminUsername = adminUsername;
        this.userPassword = userPassword;
        this.authority = authority;
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
        return adminUsername == null ? client.getClientEmail() : adminUsername;
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
