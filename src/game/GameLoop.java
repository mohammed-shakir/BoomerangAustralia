package game;

import java.util.ArrayList;
import java.util.List;

import game.GameComponents.*;
import models.cards.Deck;
import models.draft.Draft;
import models.draft.Drafting;
import models.player.Player;

import networking.*;
import scoring.AustraliaScoreCalculator;

public class GameLoop {
    private static final int ROUND_COUNT = 4; // 4 rounds in total
    private static final int DRAFT_COUNT = 6; // 6 drafts in total
    private final Drafting draftMechanism = new Draft();
    boolean throwCard = true;
    private final AustraliaScoreCalculator scoreCalculator = new AustraliaScoreCalculator();

    // Various game components
    private GameMechanics gameMechanics = new GameMechanics();
    private BroadcastManager broadcastManager = new BroadcastManager();
    private ComponentsProcessor componentsProcessor = new ComponentsProcessor();

    // Main game loop, manages game rounds and broadcasts game state to players.
    public void run() {
        List<Player> players = Server.getInstance().players;
        for (int i = 0; i < ROUND_COUNT; i++) {
            throwCard = true; // Resetting throwCard for each round

            // Initialize game state and draft initial hands for players.
            componentsProcessor.emptyPlayerHands(players);
            Deck deck = componentsProcessor.initializeDeck();
            componentsProcessor.draftInitialHandsForPlayers(players, deck);
            executeGameRound(i, players);
        }

        gameMechanics.endGameDisplay(players);

        Server.getInstance().broadcastMessage("Game Over");
    }

    // Handles individual game round logic.
    private void executeGameRound(int round, List<Player> players) {
        Server.getInstance().broadcastMessage("\nRound " + (round + 1));
        int draft = 0;

        for (draft = 0; draft < DRAFT_COUNT; draft++) {
            Server.getInstance().broadcastMessage("\nDraft " + (draft + 1) + "\n");

            broadcastManager.broadcastPlayerHands(players);

            if (draft == 0) {
                broadcastManager.broadcastChosenCards(players);
                Server.getInstance().broadcastMessage("Chose a Throw Card.");
            } else {
                broadcastManager.broadcastChosenCards(players);
                Server.getInstance().broadcastMessage("Chose a Card to keep.");
                throwCard = false;
            }

            Server.getInstance().broadcastMessage("PROMPT");

            // Gather player selections and process them.
            ArrayList<String> clientMessages = Server.getInstance().waitForClientMessages();
            componentsProcessor.processPlayerCardSelections(clientMessages, players, throwCard);

            draftMechanism.draft(players);
        }

        // Give catch card
        draftMechanism.draft(players);

        gameMechanics.endOfRoundDisplay(players, scoreCalculator);
    }
}
