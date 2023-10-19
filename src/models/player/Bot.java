package models.player;

import java.util.Random;
import models.cards.*;

public class Bot extends Player {
    private static Random random = new Random();

    public Bot(int id) {
        super(id);
    }

    public Cards chooseCard() {
        if (!hand.isEmpty()) {
            return hand.get(random.nextInt(hand.size()));
        } else {
            return null;
        }
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void setScore(int playerScore) {
        this.score = playerScore;
    }

    public void incrementScore(int valueToAdd) {
        this.score += valueToAdd;
    }
}
