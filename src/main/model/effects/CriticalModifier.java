package model.effects;

import model.Player;
import model.Round;

// Effect that changes the critical points of the leader of the user
public class CriticalModifier implements Effect {
    public static final int MAXCRITS = 32;

    private int critPoints;

    public CriticalModifier(int critPoints) {
        this.critPoints = critPoints;
    }

    @Override
    //EFFECTS: Adds give critical points to the opponent(if targetFoe) or to the player that used the move
    public void apply(Round round, boolean movedFirst) {
        Player target = round.getPlayer(movedFirst ^ targetsFoe());
        //targets first if movedFirst or targeting foe, but not both
        int currentPoints = target.getCritCounter();
        target.setCritCounter(critPoints + currentPoints);

    }

    @Override
    //EFFECTS: returns a description with the target and change(increase or reduce) based on critPoints and targetFoe.
    public String getDescription() {
        String target = targetsFoe() ? "Opponent" : "User";
        String change = critPoints < 0 ? "reduced" : "increased";
        return target + "'s critical points are " + change + " by "  + Math.abs(critPoints) + " points.";
    }

    private boolean targetsFoe() {
        return  - MAXCRITS == critPoints;
    }
}
