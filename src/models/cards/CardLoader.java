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

// Loading card data from a specified JSON file
public class CardLoader {
    public List<Cards> loadCardsFromJSON(String path, ICardFactory factory) {
        List<Cards> cardsList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream(path);
                InputStreamReader reader = new InputStreamReader(is)) {
            // Parse the provided JSON file.
            Object obj = parser.parse(reader);
            JSONArray cardsArray = (JSONArray) obj;

            for (Object cardObj : cardsArray) {
                JSONObject cardJSON = (JSONObject) cardObj;
                // Using the factory to create a card instance.
                Cards card = factory.createCard(cardJSON);
                cardsList.add(card);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return cardsList;
    }
}
