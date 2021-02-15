package model.effects;

import model.Player;
import model.Professional;

import static ui.Main.*;

//Counters that are stored in a professional to later use their final effect
public abstract class Counter implements Effect {
    protected int waitTurns; //turns until counter is able to use, -1 signifies a direct application of final effect
    protected int windowTurns; //number of turns counter is able for

    @Override
    //EFFECT: if waitTurns == -1, apply the final effect of the counter
    //        else add the counter to player 1(if usedByPlayer1) or player 2.
    public void apply(boolean usedByPlayer1) {

        Professional user = getUser(usedByPlayer1);
        int moveIndex = user.getLastMoveUsed();
        int moveCounter = user.getMoveCounters()[moveIndex];

        if (moveCounter >= windowTurns) {
            user.setMoveCounter(moveIndex,moveCounter - 1);
        } else if (waitTurns == -1 || moveCounter >= 0) {
            finalApply(usedByPlayer1);
            user.setMoveCounter(moveIndex,-1);
        } else {
            user.setMoveCounter(moveIndex,waitTurns + windowTurns - 2);
        }
    }


    public abstract void finalApply(boolean usedByPlayer1);

    public int getWindowTurns() {
        return windowTurns;
    }
}
