package game;

import java.util.ArrayList;
import java.util.List;

import models.cards.AustralianCard;
import models.cards.AustralianCardFactory;
import models.cards.Cards;
import models.cards.Deck;
import models.draft.Draft;
import models.draft.Drafting;
import models.player.Player;

import networking.*;
import networking.Server.ClientHandler;

public class GameLoop {
    private static final int LOOP_COUNT = 5;
    private static final int DRAFT_COUNT = 7;
    private final Drafting draftMechanism = new Draft();
    boolean throwCard = true;

    public void run() {
        List<Player> players = Server.getInstance().players;
        Deck deck = initializeDeck();

        draftInitialHandsForPlayers(players, deck);

        for (int i = 0; i < LOOP_COUNT; i++) {
            executeGameRound(i, players);
        }

        Server.getInstance().broadcastMessage("Game Over");
    }

    private void executeGameRound(int round, List<Player> players) {
        Server.getInstance().broadcastMessage("Round " + (round + 1));

        for (int draft = 0; draft < DRAFT_COUNT; draft++) {
            Server.getInstance().broadcastMessage("\nDraft " + (draft + 1) + "\n");

            broadcastPlayerHands(players);
            broadcastChosenCards(players);

            if (draft == 0) {
                Server.getInstance().broadcastMessage("Chose a Throw Card.");
            } else {
                Server.getInstance().broadcastMessage("Chose a Card to keep.");
                throwCard = false;
            }

            Server.getInstance().broadcastMessage("PROMPT");
            ArrayList<String> clientMessages = Server.getInstance().waitForClientMessages();
            processPlayerCardSelections(clientMessages, players);
            displayClientMessages(clientMessages);

            draftMechanism.draft(players);
        }
    }

    private void processPlayerCardSelections(ArrayList<String> messages, List<Player> players) {
        for (int i = 0; i < messages.size() && i < players.size(); i++) {
            String chosenLetter = messages.get(i);
            Player player = players.get(i);

            for (Cards card : player.hand) {
                if (card instanceof AustralianCard && ((AustralianCard) card).getLetter().equals(chosenLetter)) {
                    if (throwCard) {
                        card.setHidden(true);
                    }
                    player.chosenCards.add(card);
                    player.hand.remove(card);
                    break;
                } else {
                    Server.getInstance().sendMessageToPlayer(player.id, "Invalid card chosen");
                }
            }
        }
    }

    private Deck initializeDeck() {
        Deck deck = new Deck();
        deck.loadCardsFromJSON("/australia/cards.json", new AustralianCardFactory());
        return deck;
    }

    private void draftInitialHandsForPlayers(List<Player> players, Deck deck) {
        for (Player player : players) {
            player.hand = deck.draftCards();
        }
    }

    private void broadcastPlayerHands(List<Player> players) {
        for (Player player : players) {
            Server.getInstance().sendMessageToPlayer(player.id, "Your Hand: ");
            for (var card : player.hand) {
                Server.getInstance().sendMessageToPlayer(player.id, "Card: " + card.printCardDetails());
            }
        }
    }

    private void broadcastChosenCards(List<Player> players) {
        Server.getInstance().broadcastMessage("\n");
        for (Player player : players) {
            StringBuilder chosenCardsMessage = new StringBuilder();
            for (var chosenCard : player.chosenCards) {
                chosenCardsMessage.append(chosenCard.printCardDetails()).append(", ");
            }
            if (chosenCardsMessage.length() > 0) {
                chosenCardsMessage.setLength(chosenCardsMessage.length() - 2);
                for (ClientHandler client : Server.getInstance().clients) {
                    if (client.id == player.id) {
                        client.sendMessage("You Chose Cards: " + chosenCardsMessage.toString());
                    } else {
                        client.sendMessage("Player " + player.id + " Chose Cards: " + chosenCardsMessage.toString());
                    }
                }
            }
        }
        Server.getInstance().broadcastMessage("\n");
    }

    private void displayClientMessages(ArrayList<String> messages) {
        for (String message : messages) {
            System.out.println(message);
        }
    }
}
