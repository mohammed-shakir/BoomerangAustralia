package game.GameParts;

import models.player.Player;
import scoring.AustraliaScoreCalculator;

import java.util.List;

public interface IGameMechanics {
    void endOfRoundDisplay(List<Player> players, AustraliaScoreCalculator scoreCalculator);

    void endGameDisplay(List<Player> players);
}