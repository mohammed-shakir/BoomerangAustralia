package models.draft;

import java.util.List;
import models.player.*;
import models.cards.*;

public class Draft implements Drafting {
    @Override
    public void draft(List<Player> allPlayers) {
        List<Cards> lastPlayerCards = allPlayers.get(allPlayers.size() - 1).hand;
        List<Cards> firstPlayerCards = allPlayers.get(0).hand;

        if (lastPlayerCards.size() > 1) {
            for (int i = allPlayers.size() - 1; i > 0; i--) {
                allPlayers.get(i).hand = allPlayers.get(i - 1).hand;
            }

            allPlayers.get(0).hand = lastPlayerCards;
        } else {
            for (int i = 0; i < allPlayers.size() - 1; i++) {
                allPlayers.get(i).hand = allPlayers.get(i + 1).hand;
            }
            allPlayers.get(allPlayers.size() - 1).hand = firstPlayerCards;
        }
    }
}