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
            for (var chosenCard : player.getChosenCards()) {
                chosenCardsMessage.append(chosenCard.printCardDetails()).append(", ");
            }
            if (chosenCardsMessage.length() > 0) {
                chosenCardsMessage.setLength(chosenCardsMessage.length() - 2);
                for (ClientHandler client : Server.getInstance().clients) {
                    if (client.id == player.getId()) {
                        client.sendMessage("You Chose Cards: " + chosenCardsMessage.toString());
                    } else {
                        client.sendMessage(
                                "Player " + player.getId() + " Chose Cards: " + chosenCardsMessage.toString());
                    }
                }
            }
        }
        Server.getInstance().broadcastMessage("\n");
    }

    @Override
    public void broadcastPlayerHands(List<Player> players) {
        for (Player player : players) {
            Server.getInstance().sendMessageToPlayer(player.getId(), "Your Hand: ");
            for (var card : player.getHand()) {
                Server.getInstance().sendMessageToPlayer(player.getId(), "Card: " + card.printCardDetails());
            }
        }
    }
}
