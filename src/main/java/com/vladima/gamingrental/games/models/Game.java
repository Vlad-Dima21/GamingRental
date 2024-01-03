package com.vladima.gamingrental.games.models;

import com.vladima.gamingrental.games.dto.GameDTO;
import com.vladima.gamingrental.helpers.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table
public class Game implements BaseModel<GameDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(nullable = false, unique = true)
    private String gameName;

    @Column(nullable = false)
    private String gameGenre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameBase")
    private List<GameCopy> gameCopies;

    public Game(String gameName, String gameGenre) {
        this.gameName = gameName;
        this.gameGenre = gameGenre;
    }

    @Override
    public GameDTO toDTO() {
        return new GameDTO(gameName, gameGenre);
    }
}
