package game.GameParts;

import java.util.List;

import models.player.Player;
import networking.Server;
import networking.*;

public class BroadcastManager implements IBroadcastManager {
    @Override
    public void broadcastChosenCards(List<Player> players) {
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

    @Override
    public void broadcastPlayerHands(List<Player> players) {
        for (Player player : players) {
            Server.getInstance().sendMessageToPlayer(player.id, "Your Hand: ");
            for (var card : player.hand) {
                Server.getInstance().sendMessageToPlayer(player.id, "Card: " + card.printCardDetails());
            }
        }
    }
}
