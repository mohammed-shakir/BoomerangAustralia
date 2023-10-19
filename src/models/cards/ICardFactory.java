package models.cards;

import org.json.simple.JSONObject;

public interface ICardFactory {
    Cards createCard(JSONObject cardData);
}