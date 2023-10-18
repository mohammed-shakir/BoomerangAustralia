package models.player;

public class HumanPlayer extends Player {

    public HumanPlayer(int id) {
        super(id);
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