package game.GameParts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.cards.AustralianCard;
import models.cards.AustralianCardFactory;
import models.cards.Cards;
import models.cards.Deck;
import models.player.Bot;
import models.player.Player;
import networking.Server;

public class ComponentsProcessor implements IComponentsProcessor {
    @Override
    public void emptyPlayerHands(List<Player> players) {
        for (Player player : players) {
            player.hand.clear();
            player.chosenCards.clear();
        }
    }

    @Override
    public void draftInitialHandsForPlayers(List<Player> players, Deck deck) {
        for (Player player : players) {
            player.hand = deck.draftCards();
        }
    }

    @Override
    public Deck initializeDeck() {
        Deck deck = new Deck();
        deck.loadCardsFromJSON("/australia/cards.json", new AustralianCardFactory());
        return deck;
    }

    @Override
    public void processPlayerCardSelections(ArrayList<String> messages, List<Player> players, boolean throwCard) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String chosenLetter;

            if (player instanceof Bot) {
                chosenLetter = ((Bot) player).chooseCard();
            } else {
                chosenLetter = messages.get(i);
            }

            boolean isValidCard = false;

            while (!isValidCard) {
                Iterator<Cards> it = player.hand.iterator();
                while (it.hasNext()) {
                    Cards card = it.next();
                    if (card instanceof AustralianCard && ((AustralianCard) card).getLetter().equals(chosenLetter)) {
                        if (throwCard) {
                            card.setHidden(true);
                        }
                        player.chosenCards.add(card);
                        it.remove();
                        isValidCard = true;
                        break;
                    }
                }

                if (!isValidCard && !(player instanceof Bot)) {
                    Server.getInstance().broadcastMessage("Invalid card chosen. Try again.");
                    chosenLetter = Server.getInstance().readMessageFromClient(player.id);
                }
            }
        }
    }
}
