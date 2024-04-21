package com.vladima.gamingrental.device.models;

import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.device.dto.DeviceDTO;
import com.vladima.gamingrental.device.dto.DeviceExtrasDTO;
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
public class Device implements BaseModel<DeviceExtrasDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @Column(nullable = false)
    private int deviceNumberOfControllers;

    @Column(nullable = false, name = "device_is_available")
    private boolean deviceAvailable = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_base_id")
    private DeviceBase deviceBase;

    @OneToMany(mappedBy = "rentalDevice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> deviceRentals;

    public Device(int deviceNumberOfControllers, boolean deviceAvailable) {
        this.deviceNumberOfControllers = deviceNumberOfControllers;
        this.deviceAvailable = deviceAvailable;
    }

    public Device(int deviceNumberOfControllers, boolean deviceAvailable, DeviceBase deviceBase) {
        this.deviceNumberOfControllers = deviceNumberOfControllers;
        this.deviceAvailable = deviceAvailable;
        this.deviceBase = deviceBase;
    }

    @Override
    public DeviceExtrasDTO toDTO() {
        return new DeviceExtrasDTO(deviceNumberOfControllers, deviceAvailable, deviceId, deviceBase.getDeviceBaseName());
    }
}
