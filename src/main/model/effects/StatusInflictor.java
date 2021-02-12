package model.effects;

import model.Professional;
import model.data.NonVolatile;
import model.data.Status;
import model.data.Volatile;

import static model.data.Volatile.DRAIND;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

//Effect that inflicts a status to target professional,
public class StatusInflictor extends Counter {

    private Status status;


    //EFFECT: create a status inflictor effect, set windowTurns to 1
    public StatusInflictor(Status status, int waitTurns) {
        this.status = status;
        this.waitTurns = waitTurns;
        windowTurns = 1;
    }

    @Override
    //EFFECT: inflict status, targets self only if status is DRAIND, else target foe
    public void finalApply(boolean usedByPlayer1) {
        Professional target = (usedByPlayer1 ^ (status == DRAIND) ? PLAYER_2 : PLAYER_1).getSelectedProfessional();
        if (status.isVolatile()) {
            target.addVolatileStatus((Volatile) status);
        } else {
            target.setNonVolatileStatus((NonVolatile) status);
        }
    }

    @Override
    //EFFECT: return an exact copy of this, as to add a counter independent from this
    public Counter getCopy() {
        return new StatusInflictor(status,waitTurns);
    }
}
