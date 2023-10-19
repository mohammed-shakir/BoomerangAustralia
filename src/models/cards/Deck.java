package models.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {

    private List<Cards> cardsList = new ArrayList<>();

    public List<Cards> getCardsList() {
        return cardsList;
    }

    public void loadCards(CardLoader loader, String path, ICardFactory factory) {
        this.cardsList.addAll(loader.loadCardsFromJSON(path, factory));
    }

    // Drafts a subset of cards from the deck. Shuffles the deck before drafting and
    // removes the drafted cards from the original deck.
    public List<Cards> draftCards() {
        List<Cards> draftedCards = new ArrayList<>();
        Collections.shuffle(cardsList);
        int draftCount = Math.min(7, cardsList.size());
        for (int i = 0; i < draftCount; i++) {
            draftedCards.add(cardsList.remove(0));
        }
        return draftedCards;
    }
}
