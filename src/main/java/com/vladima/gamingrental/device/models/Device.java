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
    private int deviceNumberOfControllers;
    private boolean deviceIsAvailable;
    @ManyToOne(cascade = CascadeType.ALL)
    private DeviceBase deviceBase;
}
