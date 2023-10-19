package game.GameComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.cards.Cards;
import models.player.Player;
import networking.*;
import scoring.AustraliaScoreCalculator;

public class GameMechanics implements IGameMechanics {
    @Override
    public void endOfRoundDisplay(List<Player> players, AustraliaScoreCalculator scoreCalculator) {
        Server.getInstance().broadcastMessage("\nEnd of Round Details");

        for (Player player : players) {
            for (Cards card : player.getChosenCards()) {
                card.setHidden(false);
            }

            // Construct message detailing the cards chosen by the player
            StringBuilder playerCardsMessage = new StringBuilder();
            playerCardsMessage.append("Yout Cards: ");
            for (Cards card : player.getChosenCards()) {
                playerCardsMessage.append(card.printCardDetails()).append(", ");
            }
            if (playerCardsMessage.length() > 2) {
                playerCardsMessage.setLength(playerCardsMessage.length() - 2);
            }
            Server.getInstance().sendMessageToPlayer(player.getId(), playerCardsMessage.toString());

            int playerScore = scoreCalculator.calculateTotalScore(new ArrayList<>(player.getChosenCards()));

            for (ClientHandler client : Server.getInstance().clients) {
                if (client.id == player.getId()) {
                    client.sendMessage("Your Score: " + playerScore);
                } else {
                    client.sendMessage("Player " + player.getId() + " Score: " + playerScore);
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
        List<Player> winners = findWinners(players, highestScore);

        for (Player player : players) {
            String playerMessage;

            // Construct end game message based on whether there's a singular winner or a
            // draw
            if (winners.size() == 1) {
                playerMessage = (player.getScore() == highestScore)
                        ? "\nYou Won!\n"
                        : "\nPlayer " + winners.get(0).getId() + " Wins!\n";
            } else {
                // Handle draws between multiple players
                StringBuilder drawMessage = new StringBuilder("\nIt's a draw between players: ");
                for (Player winner : winners) {
                    drawMessage.append(winner.getId()).append(", ");
                }
                drawMessage.setLength(drawMessage.length() - 2);
                playerMessage = drawMessage.toString();
            }

            for (ClientHandler client : server.clients) {
                String scoreMessage = (client.id == player.getId())
                        ? "Your Final Score: " + player.getScore()
                        : "Player " + player.getId() + " Final Score: " + player.getScore();

                client.sendMessage(scoreMessage);
                if (client.id == player.getId() || winners.size() > 1) {
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

    // Identify the players (winners) who have the highest score.
    private List<Player> findWinners(List<Player> players, int highestScore) {
        return players.stream()
                .filter(p -> p.getScore() == highestScore)
                .collect(Collectors.toList());
    }
}
