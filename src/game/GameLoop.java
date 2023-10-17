package game;

import java.util.ArrayList;
import java.util.List;

import models.player.Player;
import networking.Server;
import models.cards.AustralianCardFactory;
import models.cards.Deck;

public class GameLoop {
    private final int LOOP_COUNT = 2;

    public GameLoop() {
    }

    public void run() {
        List<Player> players = Server.getInstance().players;
        Deck deck = new Deck();
        deck.loadCardsFromJSON("/australia/cards.json", new AustralianCardFactory());

        for (Player player : players) {
            player.hand = deck.draftCards();
            for (int i = 0; i < player.hand.size(); i++) {
                Server.getInstance().sendMessageToPlayer(player.id, player.hand.get(i).displayCard());
            }
        }

        for (int i = 0; i < LOOP_COUNT; i++) {
            Server.getInstance().broadcastMessage("Round " + (i + 1));
            System.out.println("Round " + (i + 1));

            ArrayList<String> messages = Server.getInstance().waitForClientMessages();
            for (String message : messages) {
                System.out.println(message);
            }

            Server.getInstance().broadcastMessage("Received messages for round " + (i + 1));
        }

        Server.getInstance().broadcastMessage("Game Over");
    }
}
