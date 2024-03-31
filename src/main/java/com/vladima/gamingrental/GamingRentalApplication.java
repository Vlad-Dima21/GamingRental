package com.vladima.gamingrental;

import com.vladima.gamingrental.security.models.Role;
import com.vladima.gamingrental.security.models.User;
import com.vladima.gamingrental.security.repositories.RoleRepository;
import com.vladima.gamingrental.security.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class GamingRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamingRentalApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return arg -> {
            if (roleRepository.findByAuthority("ADMIN") != null) return;
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            var admin = new User("admin", passwordEncoder.encode("admin"), adminRole);
            userRepository.save(admin);
        };
    }

}
