package scoring;

import models.cards.Cards;
import java.util.List;

public interface ScoreCalculator {
    int calculateThrowAndCatchScore(Cards throwCard, Cards catchCard);

    int calculateTouristSitesScore(List<Cards> chosenCards);

    int calculateCollectionsScore(List<Cards> chosenCards);

    int calculateAnimalsScore(List<Cards> chosenCards);

    int calculateActivitiesScore(List<Cards> chosenCards);

    int calculateTotalScore(List<Cards> chosenCards);
}