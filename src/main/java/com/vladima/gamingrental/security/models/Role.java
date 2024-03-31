package com.vladima.gamingrental.security.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String authority;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
    private List<User> roleUsers;

    public Role(String authority) {
        this.authority = authority;
    }
}
