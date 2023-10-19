package game.GameParts;

import java.util.ArrayList;
import java.util.List;

import models.cards.Cards;
import models.player.Player;
import networking.Server;
import networking.Server.ClientHandler;
import scoring.AustraliaScoreCalculator;

public class GameMechanics implements IGameMechanics {
    @Override
    public void endOfRoundDisplay(List<Player> players, AustraliaScoreCalculator scoreCalculator) {
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

    @Override
    public void endGameDisplay(List<Player> players) {
        Server server = Server.getInstance();
        server.broadcastMessage("\nEnd of Game Details");

        int highestScore = findHighestScore(players);

        for (Player player : players) {
            String playerMessage = (player.getScore() == highestScore)
                    ? "\nYou Won!\n"
                    : "\nPlayer " + findWinner(players, highestScore).id + " Wins!\n";

            for (ClientHandler client : server.clients) {
                String scoreMessage = (client.id == player.id)
                        ? "Your Final Score: " + player.getScore()
                        : "Player " + player.id + " Final Score: " + player.getScore();

                client.sendMessage(scoreMessage);
                if (client.id == player.id) {
                    client.sendMessage(playerMessage);
                }
            }
        }
    }

    private int findHighestScore(List<Player> players) {
        return players.stream()
                .mapToInt(Player::getScore)
                .max()
                .orElse(0);
    }

    private Player findWinner(List<Player> players, int highestScore) {
        return players.stream()
                .filter(p -> p.getScore() == highestScore)
                .findFirst()
                .orElse(null);
    }
}
