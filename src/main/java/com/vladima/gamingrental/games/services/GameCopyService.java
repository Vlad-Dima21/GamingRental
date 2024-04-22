package com.vladima.gamingrental.games.services;

import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.dto.GameCopyExtrasDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface GameCopyService extends BaseService<GameCopy, GameCopyExtrasDTO> {
    List<GameCopy> getAvailableCopiesForDeviceByIds(List<Long> ids, Long deviceId);
    List<GameCopyExtrasDTO> getCopies(Long gameId, Long deviceId, boolean onlyAvailable);
    void updateCopyAvailability(GameCopy gameCopy, boolean isAvailable);
}
