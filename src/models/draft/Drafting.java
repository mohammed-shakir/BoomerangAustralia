package models.draft;

import java.util.List;
import models.player.*;

public interface Drafting {
    void draft(List<Player> allPlayers);
}