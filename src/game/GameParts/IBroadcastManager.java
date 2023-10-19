package game.GameParts;

import java.util.ArrayList;
import java.util.List;

import models.player.Player;

public interface IBroadcastManager {
    void broadcastChosenCards(List<Player> players);

    void broadcastPlayerHands(List<Player> players);
}