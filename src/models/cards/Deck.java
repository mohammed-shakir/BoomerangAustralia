package models.cards;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {
    private List<Cards> cardsList;

    public Deck() {
        this.cardsList = new ArrayList<>();
    }

    public List<Cards> getCardsList() {
        return cardsList;
    }

    public void loadCardsFromJSON(String path, CardFactory factory) {
        JSONParser parser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream(path);
                InputStreamReader reader = new InputStreamReader(is)) {
            Object obj = parser.parse(reader);
            JSONArray cardsArray = (JSONArray) obj;

            for (Object cardObj : cardsArray) {
                JSONObject cardJSON = (JSONObject) cardObj;
                Cards card = factory.createCard(cardJSON);
                cardsList.add(card);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

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

interface CardFactory {
    Cards createCard(JSONObject cardData);
}
