package com.vladima.gamingrental.games.dto;

import com.vladima.gamingrental.games.models.Game;
import com.vladima.gamingrental.helpers.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameDTO implements BaseDTO<Game> {

    @NotBlank(message = "Game must have a name")
    private String gameName;

    @NotBlank(message = "Game must have a genre")
    private String gameGenre;
    @Override
    public Game toModel() {
        return new Game(gameName, gameGenre);
    }
}
