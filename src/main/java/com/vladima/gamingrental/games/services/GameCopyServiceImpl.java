package com.vladima.gamingrental.games.services;

import com.vladima.gamingrental.device.services.DeviceBaseService;
import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.repositories.GameCopyRepository;
import com.vladima.gamingrental.games.repositories.GameRepository;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import com.vladima.gamingrental.helpers.EntityOperationException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class GameCopyServiceImpl extends BaseServiceImpl<GameCopy, GameCopyDTO, GameCopyRepository> implements GameCopyService {

    private final GameRepository gameRepository;
    private final DeviceBaseService deviceBaseService;
    public GameCopyServiceImpl(GameCopyRepository repository, GameRepository gameRepository, DeviceBaseService deviceBaseService) {
        super(repository);
        this.gameRepository = gameRepository;
        this.deviceBaseService = deviceBaseService;
    }

    @Override
    public List<GameCopy> getAvailableCopiesForDeviceByIds(List<Long> ids, Long deviceId) {
        return getRepository().findActiveByDeviceAndIds(ids, deviceId);
    }

    @Override
    public List<GameCopyDTO> getCopies(Long gameId, Long deviceId, boolean onlyAvailable) {
        String incorrectModelName = null;
        if (gameId != null && !gameRepository.existsById(gameId)) incorrectModelName = "game";
        if (deviceId != null) {
            try {
                deviceBaseService.getById(deviceId);
            } catch (EntityOperationException e) {
                incorrectModelName = "device";
            }
        }
        if (incorrectModelName != null) {
            throw new EntityOperationException(
                    MessageFormat.format("Incorrect {0} ID", incorrectModelName),
                    "",
                    HttpStatus.NOT_FOUND
            );
        }
        return getRepository().findCopies(gameId, deviceId, onlyAvailable).stream().map(GameCopy::toDTO).toList();
    }

    @Override
    @Transactional
    public void updateCopyAvailability(GameCopy gameCopy, boolean isAvailable) {
        gameCopy.setAvailable(isAvailable);
    }

    //region unused
    @Override
    public GameCopyDTO create(GameCopyDTO gameCopyDTO) {
        return null;
    }

    @Override
    public GameCopyDTO updateInfo(Long id, GameCopyDTO gameCopyDTO) {
        return null;
    }
    //endregion

    @Override
    public String getLogName() {
        return "Game copy";
    }
}
