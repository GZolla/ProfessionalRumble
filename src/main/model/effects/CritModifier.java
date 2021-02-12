package model.effects;

import model.Player;
import model.data.Volatile;

import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

// Effect that changes the critical points of the leader of the user
public class CritModifier implements Effect {

    private boolean targetFoe;
    private int critPoints;

    public CritModifier(boolean targetFoe, int critPoints) {
        this.targetFoe = targetFoe;
        this.critPoints = critPoints;
    }

    @Override
    //EFFECTS: Adds give critical points to the opponent(if targetFoe) or to the player that used the move
    public void apply(boolean usedByPLayer1) {
        Player target = usedByPLayer1 ^ targetFoe ? PLAYER_1 : PLAYER_2;
        int currentPoints = target.getCritCounter();

        int min = target.getSelectedProfessional().getVolatileStatus().contains(Volatile.UNLCKY) ? 0 : -1;
        int newPoints = Math.min(8,Math.max(min,critPoints + currentPoints));

        target.setCritCounter(newPoints);

    }
}
