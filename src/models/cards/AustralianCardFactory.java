package models.cards;

import org.json.simple.JSONObject;

// Factory class responsible for creating AustralianCard instances.
public class AustralianCardFactory implements ICardFactory {
    @Override
    public Cards createCard(JSONObject cardData) {
        String name = (String) cardData.get("name");
        String letter = (String) cardData.get("letter");
        String region = (String) cardData.get("region");
        int number = ((Long) cardData.get("number")).intValue();
        String collection = (String) cardData.get("collection");
        String animal = (String) cardData.get("animal");
        String activity = (String) cardData.get("activity");

        return new AustralianCard(name, letter, region, number, collection, animal, activity, false);
    }
}
