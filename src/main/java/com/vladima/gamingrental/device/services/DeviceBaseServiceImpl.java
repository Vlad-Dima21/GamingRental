package com.vladima.gamingrental.device.services;

import com.vladima.gamingrental.device.dto.DeviceBaseExtrasDTO;
import com.vladima.gamingrental.device.models.DeviceBase;
import com.vladima.gamingrental.device.repositories.DeviceBaseRepository;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import com.vladima.gamingrental.helpers.PageableResponseDTO;
import com.vladima.gamingrental.helpers.SortDirection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceBaseServiceImpl extends BaseServiceImpl<DeviceBase, DeviceBaseExtrasDTO, DeviceBaseRepository> implements DeviceBaseService {
    private static final int PAGE_SIZE = 3;

    public DeviceBaseServiceImpl(DeviceBaseRepository repository) {
        super(repository);
    }

    @Override
    public List<DeviceBaseExtrasDTO> getByName(String name) {
        return getRepository().findByDeviceBaseNameContainingIgnoreCase(name)
                .stream()
                .map(DeviceBase::toDTO)
                .toList();
    }

    @Override
    public DeviceBase getByExactName(String name) {
        return getRepository().findByDeviceBaseName(name);
    }

    @Override
    public List<DeviceBaseExtrasDTO> getByProducer(String producer) {
        return getRepository().findByDeviceBaseProducerIgnoreCase(producer)
                .stream()
                .map(DeviceBase::toDTO)
                .toList();
    }

    @Override
    public List<DeviceBaseExtrasDTO> getByYear(int year) {
        var currentYear = LocalDateTime.now().getYear();
        if (year < 1990 || year >= currentYear) {
            throw new EntityOperationException(
                "Invalid year",
                MessageFormat.format("Year should be between 1990 and {0}", currentYear),
                HttpStatus.BAD_REQUEST
            );
        }
        return getRepository().findByDeviceBaseYearOfReleaseGreaterThanEqual(year)
                .stream()
                .map(DeviceBase::toDTO)
                .toList();
    }

    @Override
    public PageableResponseDTO<DeviceBaseExtrasDTO> getFiltered(String name, String producer, Integer year, boolean ifAvailable, int page, SortDirection sort) {
        var pageRequest = PageRequest.of(page - 1, PAGE_SIZE, sort.by("deviceBaseName"));
        var result = getRepository().findFiltered(name, producer, year, ifAvailable, pageRequest);
        return new PageableResponseDTO<>(result.getTotalPages(), result.stream().map(DeviceBase::toDTO).toList());
    }

    @Override
    public DeviceBaseExtrasDTO create(DeviceBaseExtrasDTO deviceBaseDTO) {
        var existingDevice = getRepository().findByDeviceBaseName(deviceBaseDTO.getDeviceBaseName());
        if (existingDevice != null) {
            throw new EntityOperationException(
                "Device not added",
                "A device with the same name already exists",
                HttpStatus.CONFLICT
            );
        }
        return getRepository().save(deviceBaseDTO.toModel()).toDTO();
    }

    //unused
    @Override
    @Transactional
    public DeviceBaseExtrasDTO updateInfo(Long id, DeviceBaseExtrasDTO deviceBaseDTO) {
        var device = getModelById(id);
        device.setDeviceBaseName(deviceBaseDTO.getDeviceBaseName());
        device.setDeviceBaseProducer(deviceBaseDTO.getDeviceBaseProducer());
        device.setDeviceBaseYearOfRelease(deviceBaseDTO.getDeviceBaseYearOfRelease());
        return device.toDTO();
    }

    @Override
    public String getLogName() {
        return "Device";
    }
}
