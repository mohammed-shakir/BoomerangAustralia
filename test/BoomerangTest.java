import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import game.GameComponents.ComponentsProcessor;

import java.util.ArrayList;

import models.cards.AustralianCard;
import models.cards.AustralianCardFactory;
import models.cards.CardLoader;
import models.cards.Cards;
import models.cards.Deck;
import models.player.HumanPlayer;
import models.player.Player;
import scoring.AustraliaScoreCalculator;

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
        try {
            Deck deck = new Deck();
            CardLoader loader = new CardLoader();
            deck.loadCards(loader, "/australia/cards.json", new AustralianCardFactory());
            Assert.assertEquals(28, deck.getDeckSize());
            List<Cards> nonSufflCards = deck.draftCards();
            deck.shuffle(deck.getCardsList());
            Assert.assertNotSame(nonSufflCards, deck.draftCards());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown for valid player count");
        }
    }

    // Test 4
    @Test
    public void testDealCards() {
        try {
            List<Player> players = new ArrayList<Player>();
            Player player1 = new HumanPlayer(0);
            Player player2 = new HumanPlayer(1);
            players.add(player1);
            players.add(player2);
            Deck deck = new Deck();
            CardLoader loader = new CardLoader();
            deck.loadCards(loader, "/australia/cards.json", new AustralianCardFactory());

            ComponentsProcessor processor = new ComponentsProcessor();
            processor.emptyPlayerHands(players);
            processor.draftInitialHandsForPlayers(players, deck);

            Assert.assertEquals(7, player1.getHand().size());
            Assert.assertEquals(7, player2.getHand().size());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown for valid player count");
        }
    }

    // Test 10
    @Test
    public void testCalculateScore() {
        AustraliaScoreCalculator scoreCalculator = new AustraliaScoreCalculator();

        List<Cards> cards = new ArrayList<>();

        cards.add(new AustralianCard("Uluru", "E", "Northern Territory", 4, "", "Emus", "Indigenous Culture", false));
        cards.add(
                new AustralianCard("Daintree Rainforest", "K", "Queensland", 6, "Souvenirs", "", "Bushwalking", false));
        cards.add(new AustralianCard("Blue Mountains", "Q", "New South Whales", 5, "", "Wombats", "Indigenous Culture",
                false));
        cards.add(new AustralianCard("Kangaroo Island", "O", "South Australia", 3, "", "Kangaroos", "Bushwalking",
                false));
        cards.add(new AustralianCard("The MCG", "V", "Victoria", 2, "Leaves", "", "Indigenous Culture", false));
        cards.add(new AustralianCard("Mount Wellington", "Z", "Tasmania", 7, "", "Koalas", "Sightseeing", false));
        cards.add(new AustralianCard("Bondi Beach", "S", "New South Whales", 5, "", "Wombats", "Swimming", false));

        int score1 = scoreCalculator.calculateThrowAndCatchScore(cards.get(1), cards.get(3));
        Assert.assertEquals(3, score1);

        int score2 = scoreCalculator.calculateTouristSitesScore(cards);
        Assert.assertEquals(7, score2);

        int score3 = scoreCalculator.calculateCollectionsScore(cards);
        Assert.assertEquals(12, score3);

        int score4 = scoreCalculator.calculateAnimalsScore(cards);
        Assert.assertEquals(0, score4);

        int score5 = scoreCalculator.calculateActivitiesScore(cards);
        Assert.assertEquals(4, score5);
    }
}
