package models.player;

import java.util.ArrayList;
import java.util.List;
import models.cards.*;

public abstract class Player {
    protected List<Cards> hand = new ArrayList<>();
    protected List<Cards> chosenCards = new ArrayList<>();
    protected final int id;
    protected int score = 0;

    public Player(int id) {
        this.id = id;
    }

    public List<Cards> getHand() {
        return hand;
    }

    public void setHand(List<Cards> hand) {
        this.hand = hand;
    }

    public List<Cards> getChosenCards() {
        return chosenCards;
    }

    public void setChosenCards(List<Cards> chosenCards) {
        this.chosenCards = chosenCards;
    }

    public int getId() {
        return id;
    }

    public abstract int getScore();

    public abstract void setScore(int playerScore);
}
