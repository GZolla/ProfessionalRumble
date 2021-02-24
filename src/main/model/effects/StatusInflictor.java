package model.effects;

import model.Professional;
import model.data.NonVolatile;
import model.data.Status;
import model.data.Volatile;

import static model.data.NonVolatile.UNEMP;
import static model.data.Volatile.DRAIND;
import static model.data.Volatile.NAUSEA;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

//Effect that inflicts a status to target professional,
public class StatusInflictor extends CounterSetter {

    private Status status;


    //EFFECT: create a status inflictor effect, set windowTurns to 1
    public StatusInflictor(Status status, int chargeTurns) {
        this.status = status;
        this.chargeTurns = chargeTurns;
        windowTurns = 1;
    }

    @Override
    //EFFECT: inflict status, targets self only if status is DRAIND, else target foe
    public void finalApply(boolean usedByPlayer1) {
        Professional target = (usedByPlayer1 ^ (status == DRAIND) ? PLAYER_2 : PLAYER_1).getSelectedProfessional();
        if (status.isVolatile()) {
            if (status == UNEMP || status == NAUSEA) {
                System.out.println(target.getFullName() + " became " + status.getName() + ".");
            }
            target.addVolatileStatus((Volatile) status);
        } else {
            target.setNonVolatileStatus((NonVolatile) status);
        }
    }


    @Override
    public String getDescription() {
        String action;
        if (status == DRAIND) {
            return "User will not be able to move next turn.";
        }
        if (chargeTurns == -1) {
            action = "Inflicts";
        } else {
            action =  "Has a " + chargeTurns + " counter(1 turn window) to inflict";
        }
        return action + " opponent with " + status.getName();
    }
}
