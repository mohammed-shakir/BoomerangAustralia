package game.GameComponents;

import java.util.ArrayList;
import java.util.List;

import models.cards.Deck;
import models.player.Player;

public interface IComponentsProcessor {
    void emptyPlayerHands(List<Player> players);

    void draftInitialHandsForPlayers(List<Player> players, Deck deck);

    Deck initializeDeck();

    void processPlayerCardSelections(ArrayList<String> messages, List<Player> players, boolean throwCard);
}