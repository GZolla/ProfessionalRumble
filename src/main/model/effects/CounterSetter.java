package model.effects;

import model.Player;
import model.Professional;
import model.Round;

import static ui.Main.*;

// CounterSetters set counters for the move that used them
//     - This effect must be used for a set # of turns(chargeTurns) before being able to use the final effect
//     - The move is then able to use the final effect for a window of set turns(windowTurns)
//     - If the final effect is applied or window turns pass and the move is not used, the counter is reset
public abstract class CounterSetter implements Effect {
    protected int chargeTurns; // 0 signifies a direct application of final effect
    protected int windowTurns;

    @Override
    //EFFECT: if the counter is above window turns it means the move needs charging
    //            then charge once more the move's counter (by reducing it by 1)
    //        else if either chargeTurns = -1 or the counter is able(>0; <= windowTurns guaranteed by condition above)
    //            then apply the final effect and remove the counter(by setting it to 0)
    //        else set the counter to the sum of charge turns and window turns( -1 accounts for this turn's charge)
    public void apply(Round round, boolean movedFirst) {
        Professional user = round.getUser(movedFirst);
        int moveIndex = round.getAction(movedFirst)[1] % 4;
        //Since effect only runs after being used by a move, this index represents the move used this turn
        int moveCounter = user.getMoveCounters()[moveIndex];

        if (moveCounter > windowTurns) {
            user.setMoveCounter(moveIndex,moveCounter - 1);
        } else if (chargeTurns == 0 || moveCounter > 0) {
            finalApply(round, movedFirst);
            user.setMoveCounter(moveIndex,0);
        } else {
            user.setMoveCounter(moveIndex,chargeTurns + windowTurns - 1);
            //A move must be reduced chargeTurns times before its no longer >windowTurns
            //if <=window turns it can be used but will be reduced by Professional's updateCounters() regardless
        }
    }


    public abstract void finalApply(Round round, boolean movedFirst);

    public int getWindowTurns() {
        return windowTurns;
    }
}
