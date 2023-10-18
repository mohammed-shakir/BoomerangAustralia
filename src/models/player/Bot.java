package models.player;

import java.util.Random;

public class Bot extends Player {
    private static Random random = new Random();

    public Bot(int id) {
        super(id);
    }

    public String chooseCard() {
        if (!hand.isEmpty()) {
            return hand.get(random.nextInt(hand.size())).getLetter();
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
        this.score += playerScore;
    }
}
