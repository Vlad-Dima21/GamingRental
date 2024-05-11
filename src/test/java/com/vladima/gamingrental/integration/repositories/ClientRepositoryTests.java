package com.vladima.gamingrental.integration.repositories;
import com.vladima.gamingrental.client.models.Client;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClientRepositoryTests {

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setClientName("Test Client");
        client.setClientEmail("testClient@email.com");
        client.setClientPhone("1234567890");
        entityManager.persist(client);
        entityManager.flush();
    }

    @Test
    public void whenFindByClientEmail_thenReturnClient() {
        Client found = clientRepository.findByClientEmail(client.getClientEmail());
        assertThat(found).isNotNull();
        assertThat(found.getClientEmail()).isEqualTo(client.getClientEmail());
    }

    @Test
    public void whenFindByClientEmail_thenReturnNull() {
        Client found = clientRepository.findByClientEmail("nonexistent@email.com");
        assertThat(found).isNull();
    }

    @Test
    public void whenFindByClientPhone_thenReturnClient() {
        Client found = clientRepository.findByClientPhone(client.getClientPhone());
        assertThat(found).isNotNull();
        assertThat(found.getClientPhone()).isEqualTo(client.getClientPhone());
    }

    @Test
    public void whenFindByClientPhone_thenReturnNull() {
        Client found = clientRepository.findByClientPhone("0987654321");
        assertThat(found).isNull();
    }

    @Test
    public void whenFindByClientNameContaining_thenReturnClients() {
        Page<Client> found = clientRepository.findByClientNameContaining(client.getClientName(), PageRequest.of(0, 10));
        assertThat(found).isNotNull();
        assertThat(found.getContent()).isNotEmpty();
    }

    @Test
    public void whenFindByClientNameContaining_thenReturnEmpty() {
        Page<Client> found = clientRepository.findByClientNameContaining("Nonexistent Client", PageRequest.of(0, 10));
        assertThat(found).isNotNull();
        assertThat(found.getContent()).isEmpty();
    }

    @Test
    public void whenFindByClientName_thenReturnClient() {
        Client found = clientRepository.findByClientName(client.getClientName());
        assertThat(found).isNotNull();
        assertThat(found.getClientName()).isEqualTo(client.getClientName());
    }

    @Test
    public void whenFindByClientName_thenReturnNull() {
        Client found = clientRepository.findByClientName("Nonexistent Client");
        assertThat(found).isNull();
    }
}