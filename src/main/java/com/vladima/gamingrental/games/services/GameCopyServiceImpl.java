package com.vladima.gamingrental.games.services;

import com.vladima.gamingrental.games.dto.GameCopyDTO;
import com.vladima.gamingrental.games.models.GameCopy;
import com.vladima.gamingrental.games.repositories.GameCopyRepository;
import com.vladima.gamingrental.helpers.BaseServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameCopyServiceImpl extends BaseServiceImpl<GameCopy, GameCopyDTO, GameCopyRepository> implements GameCopyService {
    public GameCopyServiceImpl(GameCopyRepository repository) {
        super(repository);
    }

    @Override
    public List<GameCopy> getAvailableModelsByIds(List<Long> ids, Long deviceId) {
        return getRepository().findActiveByIds(ids, deviceId);
    }

    @Override
    @Transactional
    public void updateCopyAvailibility(GameCopy gameCopy, boolean isAvailable) {
        gameCopy.setAvailable(isAvailable);
    }

    @Override
    public GameCopyDTO create(GameCopyDTO gameCopyDTO) {
        return null;
    }

    @Override
    public GameCopyDTO updateInfo(Long id, GameCopyDTO gameCopyDTO) {
        return null;
    }

    @Override
    public String getLogName() {
        return "Game copy";
    }
}
