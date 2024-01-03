package com.vladima.gamingrental.games.repositories;

import com.vladima.gamingrental.games.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByGameName(String name);
}
