package com.vladima.gamingrental;

import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.security.models.Role;
import com.vladima.gamingrental.security.models.User;
import com.vladima.gamingrental.security.repositories.RoleRepository;
import com.vladima.gamingrental.security.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
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
            Role adminRole = roleRepository.findByAuthority("ADMIN");
            if (adminRole == null) {
                adminRole = roleRepository.save(new Role("ADMIN"));
            }
            var admin = new User("admin", passwordEncoder.encode("admin"), adminRole);
            userRepository.save(admin);


            Role clientRole = roleRepository.findByAuthority("CLIENT");
            if (clientRole == null) {
                clientRole = roleRepository.save(new Role("CLIENT"));
            }
            if (userRepository.findByClient_ClientEmail("test@email.com").isPresent()) {
                return;
            }
            var testUser = new User(null, passwordEncoder.encode("test"), null, null, clientRole);
            var testClient = new Client("test", "test@email.com", "0201732.511-", testUser);
            testUser.setClient(testClient);
            userRepository.save(testUser);
        };
    }

}
