package com.vladima.gamingrental.device.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @Column(nullable = false)
    private int deviceNumberOfControllers;

    @Column(nullable = false)
    private boolean deviceIsAvailable = true;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_base_id")
    private DeviceBase deviceBase;
}
