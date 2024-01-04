package com.vladima.gamingrental.client.services.implementations;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import com.vladima.gamingrental.client.models.Rental;
import com.vladima.gamingrental.client.repositories.RentalRepository;
import com.vladima.gamingrental.client.services.ClientService;
import com.vladima.gamingrental.client.services.RentalService;
import com.vladima.gamingrental.device.models.Device;
import com.vladima.gamingrental.device.services.DeviceService;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.services.GameCopyService;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalServiceImpl extends BaseServiceImpl<Rental, RentalDTO, RentalRepository> implements RentalService {

    private final ClientService clientService;
    private final DeviceService deviceService;
    private final GameCopyService gameCopyService;

    public RentalServiceImpl(RentalRepository repository, ClientService clientService, DeviceService deviceService, GameCopyService gameCopyService) {
        super(repository);
        this.clientService = clientService;
        this.deviceService = deviceService;
        this.gameCopyService = gameCopyService;
    }

    @Override
    public List<RentalDTO> getRentals(String clientName, String deviceName, Boolean returned, boolean pastDue) {
        if (clientName != null) clientService.getByExactName(clientName);
        if (deviceName != null && deviceService.getByDeviceBaseName(deviceName, false).isEmpty()) {
            throw new EntityOperationException(
                "Device not found",
                MessageFormat.format("No such device as {0}", deviceName),
                HttpStatus.BAD_REQUEST
            );
        }
        return getRepository().getRentals(clientName, deviceName, returned, pastDue)
                .stream().map(Rental::toDTO)
                .toList();
    }

    public RentalDTO createRequest(RentalRequestDTO rentalRequestDTO) {
        var client = clientService.getModelById(rentalRequestDTO.getClientId());
        Device device = deviceService.getModelById(rentalRequestDTO.getDeviceUnitId(), true);
        List<GameCopy> gameCopies = new ArrayList<>();

        gameCopies = gameCopyService.getAvailableModelsByIds(rentalRequestDTO.getGameCopiesId(), device.getDeviceBase().getDeviceBaseId());
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
                        HttpStatus.BAD_REQUEST
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
        return rental.toDTO();
    }

    private RentalDTO create(Rental rental) {
        var saved = getRepository().save(rental);
        if (saved.getRentalDevice() != null) deviceService.updateDeviceAvailability(saved.getRentalDevice().getDeviceId(), false);
        saved.getRentalGames().forEach(gc -> gameCopyService.updateCopyAvailibility(gc, false));
        return saved.toDTO();
    }

    //region unused

    @Override
    public RentalDTO create(RentalDTO rentalDTO) {
        return null;
    }

    @Override
    public List<RentalDTO> getAll() {
        return null;
    }

    @Override
    public Rental getModelById(Long id) {
        return null;
    }

    @Override
    public RentalDTO getById(Long id) {
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
