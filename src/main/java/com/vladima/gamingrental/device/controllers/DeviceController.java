package com.vladima.gamingrental.device.controllers;

import com.vladima.gamingrental.device.dto.DeviceExtrasDTO;
import com.vladima.gamingrental.device.services.DeviceService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/of-id/{id}")
    public ResponseEntity<List<DeviceExtrasDTO>> getByDeviceBaseId(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(deviceService.getByDeviceBaseId(id), HttpStatus.OK);
    }

    @GetMapping("/of/{name}")
    public ResponseEntity<List<DeviceExtrasDTO>> getByDeviceBaseName(@PathVariable String name) {
        return new ResponseEntity<>(deviceService.getByDeviceBaseName(name), HttpStatus.OK);
    }
}
