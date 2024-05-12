package com.vladima.gamingrental.unit.repositories;

import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeviceBaseRepositoryTests {

    @TestConfiguration
    static class Configuration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeviceBaseRepository deviceBaseRepository;

    private DeviceBase deviceBase;


    @BeforeEach
    public void init() {
        deviceBase = new DeviceBase();
        deviceBase.setDeviceBaseName("Test Device Base");
        deviceBase.setDeviceBaseProducer("Test Device Base Producer");
        deviceBase.setDeviceBaseYearOfRelease(2021);
        entityManager.persistAndFlush(deviceBase);
    }

    @Test
    public void findDeviceBaseByName_returnsDeviceBase() {
        DeviceBase foundDeviceBase = deviceBaseRepository.findByDeviceBaseName(deviceBase.getDeviceBaseName());
        assertThat(foundDeviceBase).isNotNull();
        assertThat(foundDeviceBase.getDeviceBaseName()).isEqualTo(deviceBase.getDeviceBaseName());
    }

    @Test
    public void findDeviceBaseByName_returnsNullWhenNotFound() {
        DeviceBase foundDeviceBase = deviceBaseRepository.findByDeviceBaseName("Nonexistent Device Base");
        assertThat(foundDeviceBase).isNull();
    }

    @Test
    public void saveDeviceBase_persistsDeviceBase() {
        DeviceBase newDeviceBase = new DeviceBase();
        newDeviceBase.setDeviceBaseName("New Device Base");
        newDeviceBase.setDeviceBaseProducer("New Device Base Producer");
        newDeviceBase.setDeviceBaseYearOfRelease(2022);
        deviceBaseRepository.save(newDeviceBase);
        assertThat(deviceBaseRepository.findByDeviceBaseName(newDeviceBase.getDeviceBaseName())).isEqualTo(newDeviceBase);
    }

    @Test
    public void deleteDeviceBase_removesDeviceBase() {
        deviceBaseRepository.delete(deviceBase);
        assertThat(deviceBaseRepository.findByDeviceBaseName(deviceBase.getDeviceBaseName())).isNull();
    }
}
