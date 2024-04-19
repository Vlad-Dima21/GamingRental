package com.vladima.gamingrental.device.repositories;

import com.vladima.gamingrental.device.models.DeviceBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceBaseRepository extends JpaRepository<DeviceBase, Long> {
    List<DeviceBase> findByDeviceBaseNameContainingIgnoreCase(String name);
    DeviceBase findByDeviceBaseName(String name);
    List<DeviceBase> findByDeviceBaseProducerIgnoreCase(String name);
    List<DeviceBase> findByDeviceBaseYearOfReleaseGreaterThanEqual(int deviceBaseYearOfRelease);
}
