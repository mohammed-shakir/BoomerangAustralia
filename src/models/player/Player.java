package models.player;

import java.util.ArrayList;
import java.util.List;
import models.cards.*;

public abstract class Player {
    public List<Cards> hand = new ArrayList<>();
    public List<Cards> chosenCards = new ArrayList<>();
    public int id;
    protected int score = 0;

    public Player(int id) {
        this.id = id;
    }

    public abstract int getScore();

    public abstract void setScore(int playerScore);
}
