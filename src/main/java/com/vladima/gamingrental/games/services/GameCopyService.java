package com.vladima.gamingrental.games.services;

import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.helpers.BaseService;

import java.util.List;

public interface GameCopyService extends BaseService<GameCopy, GameCopyDTO> {
    List<GameCopy> getAvailableModelsByIds(List<Long> ids);

    void updateCopyAvailibility(GameCopy gameCopy, boolean isActive);
}
