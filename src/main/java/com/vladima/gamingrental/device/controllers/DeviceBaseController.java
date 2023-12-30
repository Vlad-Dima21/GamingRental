package com.vladima.gamingrental.device.controllers;

import com.vladima.gamingrental.device.dto.DeviceBaseDTO;
import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.device.services.DeviceBaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceBaseController {

    private final DeviceBaseService deviceBaseService;

    @GetMapping("/{id}")
    public ResponseEntity<DeviceBaseExtrasDTO> getDeviceById(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<>(deviceBaseService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DeviceBaseExtrasDTO>> getFilteredDevices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "false") boolean ifAvailable
    ) {
        return new ResponseEntity<>(deviceBaseService.getFiltered(name, producer, year, ifAvailable), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<DeviceBaseDTO> createDevice(@Valid @RequestBody DeviceBaseDTO deviceBaseDTO) {
        return new ResponseEntity<>(deviceBaseService.create((DeviceBaseExtrasDTO) deviceBaseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable @Min(1) Long id) {
        deviceBaseService.removeById(id);
        return new ResponseEntity<>("Device deleted", HttpStatus.NO_CONTENT);
    }
}
