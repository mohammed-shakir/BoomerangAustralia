import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import models.cards.AustralianCardFactory;
import models.cards.CardLoader;
import models.cards.Cards;
import models.cards.Deck;

public class BoomerangTest {

    // Test 1
    @Before
    public void setup() {
        BoomerangApp.TEST_1 = true;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPlayerCountTooHigh() {
        BoomerangApp app = new BoomerangApp();
        app.startServer(8080, 5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPlayerCountTooLow() {
        BoomerangApp app = new BoomerangApp();
        app.startServer(8080, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePlayerCount() {
        BoomerangApp app = new BoomerangApp();
        app.startServer(8080, -1, 3);
    }

    @Test
    public void testValidPlayerCount() {
        BoomerangApp app = new BoomerangApp();
        try {
            app.startServer(8080, 2, 1);
        } catch (IllegalArgumentException e) {
            Assert.fail("Exception should not be thrown for valid player count");
        }
    }

    @After
    public void cleanup() {
        BoomerangApp.TEST_1 = false;
    }

    // Test 2 and 3
    @Test
    public void testDeck() {
        Deck deck = new Deck();
        CardLoader loader = new CardLoader();
        deck.loadCards(loader, "/australia/cards.json", new AustralianCardFactory());
        Assert.assertEquals(28, deck.getDeckSize());
        List<Cards> nonSufflCards = deck.draftCards();
        deck.shuffle(deck.getCardsList());
        Assert.assertNotSame(nonSufflCards, deck.draftCards());
    }
}
