package com.vladima.gamingrental.games.services;

import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface GameCopyService extends BaseService<GameCopy, GameCopyDTO> {
    List<GameCopy> getAvailableCopiesForDeviceByIds(List<Long> ids, Long deviceId);
    List<GameCopyDTO> getCopies(Long gameId, Long deviceId, boolean onlyAvailable);
    void updateCopyAvailibility(GameCopy gameCopy, boolean isActive);
}
