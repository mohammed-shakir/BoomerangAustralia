package models.draft;

import java.util.List;
import models.player.*;
import models.cards.*;

public class Draft implements Drafting {

    private void lastToFirst(List<Player> allPlayers) {
        List<Cards> lastPlayerCards = allPlayers.get(allPlayers.size() - 1).hand;
        for (int i = allPlayers.size() - 1; i > 0; i--) {
            allPlayers.get(i).hand = allPlayers.get(i - 1).hand;
        }
        allPlayers.get(0).hand = lastPlayerCards;
    }

    private void firstToLast(List<Player> allPlayers) {
        List<Cards> firstPlayerCards = allPlayers.get(0).hand;
        for (int i = 0; i < allPlayers.size() - 1; i++) {
            allPlayers.get(i).hand = allPlayers.get(i + 1).hand;
        }
        allPlayers.get(allPlayers.size() - 1).hand = firstPlayerCards;
    }

    @Override
    public void draft(List<Player> allPlayers) {
        if (allPlayers.get(allPlayers.size() - 1).hand.size() > 1) {
            lastToFirst(allPlayers);
        } else {
            firstToLast(allPlayers);
        }
    }
}