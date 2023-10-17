package models.player;

import java.util.ArrayList;
import java.util.List;
import models.cards.*;

public abstract class Player {
    public List<Cards> hand = new ArrayList<>();
    public static List<Cards> toDraft = new ArrayList<>();
    public int id;

    public Player(int id) {
        this.id = id;
    }
}