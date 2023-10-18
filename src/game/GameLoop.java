package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.cards.AustralianCard;
import models.cards.AustralianCardFactory;
import models.cards.Cards;
import models.cards.Deck;
import models.draft.Draft;
import models.draft.Drafting;
import models.player.Bot;
import models.player.Player;

import networking.*;
import networking.Server.ClientHandler;
import scoring.AustraliaScoreCalculator;

public class GameLoop {
    private static final int ROUND_COUNT = 2; // 4 rounds in total
    private static final int DRAFT_COUNT = 3;
    private final Drafting draftMechanism = new Draft();
    boolean throwCard = true;
    private final AustraliaScoreCalculator scoreCalculator = new AustraliaScoreCalculator();

    public void run() {
        List<Player> players = Server.getInstance().players;

        for (int i = 0; i < ROUND_COUNT; i++) {
            throwCard = true;
            emptyPlayerHands(players);
            Deck deck = initializeDeck();
            draftInitialHandsForPlayers(players, deck);
            executeGameRound(i, players);
        }

        endGameDisplay(players);

        Server.getInstance().broadcastMessage("Game Over");
    }

    private void executeGameRound(int round, List<Player> players) {
        Server.getInstance().broadcastMessage("\nRound " + (round + 1));
        int draft = 0;

        for (draft = 0; draft < DRAFT_COUNT; draft++) {
            Server.getInstance().broadcastMessage("\nDraft " + (draft + 1) + "\n");

            broadcastPlayerHands(players);

            if (draft == 0) {
                broadcastChosenCards(players);
                Server.getInstance().broadcastMessage("Chose a Throw Card.");
            } else {
                broadcastChosenCards(players);
                Server.getInstance().broadcastMessage("Chose a Card to keep.");
                throwCard = false;
            }

            Server.getInstance().broadcastMessage("PROMPT");
            ArrayList<String> clientMessages = Server.getInstance().waitForClientMessages();
            processPlayerCardSelections(clientMessages, players);
            displayClientMessages(clientMessages);

            draftMechanism.draft(players);
        }

        // Give catch card
        draftMechanism.draft(players);

        endOfRoundDisplay(players);
    }

    private void endOfRoundDisplay(List<Player> players) {
        Server.getInstance().broadcastMessage("\nEnd of Round Details");

        for (Player player : players) {
            for (Cards card : player.chosenCards) {
                card.setHidden(false);
            }

            StringBuilder playerCardsMessage = new StringBuilder();
            playerCardsMessage.append("Yout Cards: ");
            for (Cards card : player.chosenCards) {
                playerCardsMessage.append(card.printCardDetails()).append(", ");
            }
            if (playerCardsMessage.length() > 2) {
                playerCardsMessage.setLength(playerCardsMessage.length() - 2);
            }
            Server.getInstance().sendMessageToPlayer(player.id, playerCardsMessage.toString());

            int playerScore = scoreCalculator.calculateTotalScore(new ArrayList<>(player.chosenCards));

            for (ClientHandler client : Server.getInstance().clients) {
                if (client.id == player.id) {
                    client.sendMessage("Your Score: " + playerScore);
                } else {
                    client.sendMessage("Player " + player.id + " Score: " + playerScore);
                }
            }

            player.setScore(playerScore);
        }
    }

    private void processPlayerCardSelections(ArrayList<String> messages, List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String chosenLetter;

            if (player instanceof Bot) {
                chosenLetter = ((Bot) player).chooseCard();
            } else {
                chosenLetter = messages.get(i);
            }

            Iterator<Cards> it = player.hand.iterator();
            while (it.hasNext()) {
                Cards card = it.next();
                if (card instanceof AustralianCard && ((AustralianCard) card).getLetter().equals(chosenLetter)) {
                    if (throwCard) {
                        card.setHidden(true);
                    }
                    player.chosenCards.add(card);
                    it.remove();
                    break;
                } else if (!(player instanceof Bot)) {
                    Server.getInstance().sendMessageToPlayer(player.id, "Invalid card chosen");
                }
            }
        }
    }

    private void endGameDisplay(List<Player> players) {
        Server.getInstance().broadcastMessage("\nEnd of Game Details");

        for (Player player : players) {
            for (ClientHandler client : Server.getInstance().clients) {
                if (client.id == player.id) {
                    client.sendMessage("Your Final Score: " + player.getScore());
                } else {
                    client.sendMessage("Player " + player.id + " Final Score: " + player.getScore());
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

    private void emptyPlayerHands(List<Player> players) {
        for (Player player : players) {
            player.hand.clear();
            player.chosenCards.clear();
        }
    }

    private void displayClientMessages(ArrayList<String> messages) {
        for (String message : messages) {
            System.out.println(message);
        }
    }
}
