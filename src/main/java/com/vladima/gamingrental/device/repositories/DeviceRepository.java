package com.vladima.gamingrental.device.repositories;

import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.models.DeviceBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d FROM Device d JOIN DeviceBase db ON db = d.deviceBase WHERE db.deviceBaseId = :id")
    List<Device> findByDeviceBaseId(Long id);

    @Query("SELECT d FROM Device d JOIN DeviceBase db ON db = d.deviceBase WHERE db.deviceBaseName = :name")
    List<Device> findByDeviceBaseName(String name);
}
