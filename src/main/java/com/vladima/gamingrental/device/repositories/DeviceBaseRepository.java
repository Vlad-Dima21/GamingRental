package com.vladima.gamingrental.device.repositories;

import com.vladima.gamingrental.device.models.DeviceBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceBaseRepository extends JpaRepository<DeviceBase, Long> {
    List<DeviceBase> findByDeviceBaseNameContainingIgnoreCase(String name);
    DeviceBase findByDeviceBaseName(String name);
    List<DeviceBase> findByDeviceBaseProducerIgnoreCase(String name);
    List<DeviceBase> findByDeviceBaseYearOfReleaseGreaterThanEqual(int deviceBaseYearOfRelease);

    @Query("SELECT db FROM DeviceBase db " +
            "WHERE db.deviceBaseName ILIKE CONCAT('%', COALESCE(:name, '') , '%')" +
            "AND db.deviceBaseProducer ILIKE CONCAT('%', COALESCE(:producer, '') , '%')" +
            "AND COALESCE((db.deviceBaseYearOfRelease = :year), true) " +
            "AND (:ifAvailable = false OR EXISTS (SELECT dbv FROM db.devices dbv WHERE dbv.deviceAvailable ))")
    Page<DeviceBase> findFiltered(
            String name, String producer, Integer year, boolean ifAvailable, PageRequest pageRequest
    );
}
