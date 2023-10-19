package models.draft;

import java.util.List;
import models.player.*;
import models.cards.*;

public class Draft implements Drafting {

    private void lastToFirst(List<Player> allPlayers) {
        List<Cards> lastPlayerCards = allPlayers.get(allPlayers.size() - 1).getHand();
        for (int i = allPlayers.size() - 1; i > 0; i--) {
            allPlayers.get(i).setHand(allPlayers.get(i - 1).getHand());
        }
        allPlayers.get(0).setHand(lastPlayerCards);
    }

    private void firstToLast(List<Player> allPlayers) {
        List<Cards> firstPlayerCards = allPlayers.get(0).getHand();
        for (int i = 0; i < allPlayers.size() - 1; i++) {
            allPlayers.get(i).setHand(allPlayers.get(i + 1).getHand());
        }
        allPlayers.get(allPlayers.size() - 1).setHand(firstPlayerCards);
    }

    @Override
    public void draft(List<Player> allPlayers) {
        if (allPlayers.get(allPlayers.size() - 1).getHand().size() > 1) {
            lastToFirst(allPlayers);
        } else {
            firstToLast(allPlayers);
        }
    }
}