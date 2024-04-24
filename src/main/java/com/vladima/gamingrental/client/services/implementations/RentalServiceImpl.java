package com.vladima.gamingrental.client.services.implementations;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.client.repositories.ClientRepository;
import com.vladima.gamingrental.client.repositories.RentalRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.client.services.RentalService;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.services.DeviceService;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.services.GameCopyService;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalServiceImpl extends BaseServiceImpl<Rental, RentalDTO, RentalRepository> implements RentalService {

    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final DeviceService deviceService;
    private final GameCopyService gameCopyService;

    private final int PAGE_SIZE = 10;

    public RentalServiceImpl(RentalRepository repository, ClientService clientService, ClientRepository clientRepository, DeviceService deviceService, GameCopyService gameCopyService) {
        super(repository);
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.deviceService = deviceService;
        this.gameCopyService = gameCopyService;
}

    @Override
    public PageableResponseDTO<RentalDTO> getRentals(String clientEmail, String deviceName, Boolean returned, boolean pastDue, Integer page, SortDirection sort) {
        var pageRequest = PageRequest.of(page != null ? page - 1 : 0, PAGE_SIZE);
        if (clientEmail != null) clientService.getByEmail(clientEmail);
        if (deviceName != null && deviceService.getByDeviceBaseName(deviceName, false).isEmpty()) {
            throw new EntityOperationException(
                "Device not found",
                MessageFormat.format("No such device as {0}", deviceName),
                HttpStatus.NOT_FOUND
            );
        }
        pageRequest = sort != null ? pageRequest.withSort(sort.by("rentalReturnDate")): pageRequest;
        var rentals = getRepository().getRentals(clientEmail, deviceName, returned, pastDue, pageRequest);
        return new PageableResponseDTO<>(rentals.getTotalPages(), rentals.map(Rental::toDTO).toList());
    }

    public RentalDTO createRental(String clientEmail, RentalRequestDTO rentalRequestDTO) {
//        var client = clientService.getModelById(rentalRequestDTO.getClientId());
        var client = clientRepository.findByClientEmail(clientEmail);
        Device device = deviceService.getModelById(rentalRequestDTO.getDeviceUnitId(), true);
        List<GameCopy> gameCopies = new ArrayList<>();

        gameCopies = gameCopyService.getAvailableCopiesForDeviceByIds(rentalRequestDTO.getGameCopiesId(), device.getDeviceBase().getDeviceBaseId());
        if (rentalRequestDTO.getGameCopiesId().size() != gameCopies.size()) {
            throw new EntityOperationException(
                "Invalid game copies",
                "Copies may not exist or be unavailable",
                HttpStatus.BAD_REQUEST
            );
        }

        return create(new Rental(
            LocalDateTime.now().plusDays(rentalRequestDTO.getNumberOfDays()),
            null,
            client,
            device,
            gameCopies
        ));
    }

    @Override
    @Transactional
    public RentalDTO rentalReturned(Long id) {
        var rental = getRepository().findById(id).orElseThrow(() ->
                new EntityOperationException(
                        "Rental not found",
                        "",
                        HttpStatus.NOT_FOUND
                )
        );
        if (rental.getRentalReturnDate() != null) {
            throw new EntityOperationException(
                "Rental already returned",
                MessageFormat.format("Was returned on {0}",rental.getRentalReturnDate().toLocalDate()),
                HttpStatus.BAD_REQUEST
            );
        }
        rental.setRentalReturnDate(LocalDateTime.now());
        rental.getRentalDevice().setDeviceAvailable(true);
        rental.getRentalGames().forEach(gc -> gc.setAvailable(true));
        return rental.toDTO();
    }

    private RentalDTO create(Rental rental) {
        var saved = getRepository().save(rental);
        if (saved.getRentalDevice() != null) deviceService.updateDeviceAvailability(saved.getRentalDevice().getDeviceId(), false);
        saved.getRentalGames().forEach(gc -> gameCopyService.updateCopyAvailability(gc, false));
        return saved.toDTO();
    }

    //region unused

    @Override
    public RentalDTO create(RentalDTO rentalDTO) {
        return null;
    }

    @Override
    public RentalDTO updateInfo(Long id, RentalDTO rentalDTO) {
        return null;
    }

    @Override
    public void removeById(Long id) {

    }
    //endregion

    @Override
    public String getLogName() {
        return "Rental";
    }
}
