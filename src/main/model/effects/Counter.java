package model.effects;

import model.Player;

import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

//Counters that are stored in a professional to later use their final effect
public abstract class Counter implements Effect {
    protected int waitTurns; //turns until counter is able to use, -1 signifies a direct application of final effect
    protected int windowTurns; //number of turns counter is able for

    @Override
    //EFFECT: if waitTurns == -1, apply the final effect of the counter
    //        else add the counter to player 1(if usedByPlayer1) or player 2.
    public void apply(boolean usedByPlayer1) {
        if (waitTurns == -1) {
            finalApply(usedByPlayer1);
        } else {
            Player target = usedByPlayer1 ? PLAYER_1 : PLAYER_2;
            target.getSelectedProfessional().addCounter(getCopy());
        }
    }


    public abstract void finalApply(boolean usedByPlayer1);

    public abstract Counter getCopy();


    //MODIFIES: this
    //EFFECT: Reduce waitTurns by one if >0, else reduce windowTurns by 1, if both are zero return true
    public boolean update() {
        if (waitTurns > 0) {
            waitTurns -= 1;
        } else {
            windowTurns -= 1;
            if (windowTurns == 0) {
                return true;
            }
        }
        return false;

    }

    //EFFECT: Checks if have window turns and no waiting turns
    public boolean isAble() {
        return (waitTurns == 0) && (windowTurns > 0);
    }

    public int getWaitTurns() {
        return waitTurns;
    }

    public int getWindowTurns() {
        return windowTurns;
    }
}
