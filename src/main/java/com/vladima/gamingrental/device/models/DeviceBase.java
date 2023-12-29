package com.vladima.gamingrental.device.models;

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
public class DeviceBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceBaseId;
    private String deviceBaseName;
    private String deviceBaseProducer;
    private int deviceBaseYearOfRelease;
    @OneToMany
    private List<Device> devices;
}
