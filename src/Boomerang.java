import java.util.*;

import models.cards.AustralianCard;
import models.cards.AustralianCardFactory;
import models.cards.Deck;
import networking.*;
import game.*;

public class Boomerang {

    public Boomerang(String[] args) throws Exception {
        if (args.length == 1) {
            Server server = new Server(Integer.parseInt(args[0]));
            server.listenToClients(2);
            GameLoop serverGameLoop = new GameLoop(server);
            serverGameLoop.run();
        } else if (args.length == 2) {
            Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.awaitMessageFromServer();
            // client.sendMessage("Joining game");
            Deck deck = new Deck();
            deck.loadCardsFromJSON("/australia/cards.json", new AustralianCardFactory());
            for (int i = 0; i < deck.getCardsList().size(); i++) {
                ((AustralianCard) deck.getCardsList().get(i)).displayCard();
            }
        } else {
            System.out.println("Usage: java Boomerang <port> or java Boomerang <ip> <port>");
        }
    }

    public static void main(String[] args) {
        try {
            new Boomerang(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
