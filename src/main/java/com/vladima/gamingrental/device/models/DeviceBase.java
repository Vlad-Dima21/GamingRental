package com.vladima.gamingrental.device.models;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class DeviceBase implements BaseModel<DeviceBaseExtrasDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceBaseId;

    @Column(nullable = false, unique = true)
    private String deviceBaseName;

    @Column(nullable = false)
    private String deviceBaseProducer;

    private int deviceBaseYearOfRelease;

    @Column(length = 300)
    private String deviceBaseImageUrl;

    @OneToMany(mappedBy = "deviceBase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Device> devices;

    @OneToMany(mappedBy = "gameDevice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameCopy> deviceGameCopies;

    public DeviceBase(String deviceBaseName, String deviceBaseProducer, int deviceBaseYearOfRelease) {
        this.deviceBaseName = deviceBaseName;
        this.deviceBaseProducer = deviceBaseProducer;
        this.deviceBaseYearOfRelease = deviceBaseYearOfRelease;
    }

    public DeviceBase(String deviceBaseName, String deviceBaseProducer, int deviceBaseYearOfRelease, String deviceBaseImageUrl, List<Device> devices, List<GameCopy> deviceGameCopies) {
        this.deviceBaseName = deviceBaseName;
        this.deviceBaseProducer = deviceBaseProducer;
        this.deviceBaseYearOfRelease = deviceBaseYearOfRelease;
        this.deviceBaseImageUrl = deviceBaseImageUrl;
        this.devices = devices;
        this.deviceGameCopies = deviceGameCopies;
    }

    @Override
    public DeviceBaseExtrasDTO toDTO() {
        int availableUnits = 0;
        if (devices != null) {
            availableUnits = (int) devices.stream().filter(Device::isDeviceAvailable).count();
        }
        return new DeviceBaseExtrasDTO(deviceBaseId, deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease, availableUnits, deviceBaseImageUrl,
                devices != null ? devices.stream().map(Device::toDTO).toList() : List.of(),
                deviceGameCopies != null ? deviceGameCopies.stream().map(GameCopy::toDTO).toList() : List.of());
    }

    public DeviceBaseDTO toBaseDTO() {
        return new DeviceBaseDTO(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease);
    }
}
