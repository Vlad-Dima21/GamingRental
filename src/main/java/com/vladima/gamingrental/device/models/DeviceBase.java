package com.vladima.gamingrental.device.models;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
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

    @OneToMany
    @JoinColumn(name = "device_base_id")
    private List<Device> devices;

    public DeviceBase(String deviceBaseName, String deviceBaseProducer, int deviceBaseYearOfRelease) {
        this.deviceBaseName = deviceBaseName;
        this.deviceBaseProducer = deviceBaseProducer;
        this.deviceBaseYearOfRelease = deviceBaseYearOfRelease;
    }

    @Override
    public DeviceBaseExtrasDTO toDTO() {
        return new DeviceBaseExtrasDTO(deviceBaseName, deviceBaseProducer, deviceBaseYearOfRelease, devices != null ? devices.size() : 0);
    }
}
