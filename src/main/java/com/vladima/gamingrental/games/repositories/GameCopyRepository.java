package com.vladima.gamingrental.games.repositories;

import com.vladima.gamingrental.games.models.GameCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameCopyRepository extends JpaRepository<GameCopy, Long> {
    @Query("SELECT gc FROM GameCopy gc JOIN Game g where g.gameName = :name")
    List<GameCopy> findByGameName(String name);

    @Query("SELECT gc FROM GameCopy gc JOIN Game g where g.gameId = :id")
    List<GameCopy> findByGameId(Long id);

    @Query("SELECT gc FROM GameCopy gc WHERE gc.isAvailable AND gc.gameCopyId IN (:ids)")
    List<GameCopy> findActiveByIds(List<Long> ids);
}
