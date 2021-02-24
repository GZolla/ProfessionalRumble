package model.effects;

import model.Professional;
import model.Round;
import model.data.Volatile;
import model.moves.Damaging;
import model.moves.Move;

import static model.data.Volatile.CHARGE;
import static model.data.Volatile.DUGIN;

//Establishes a condition to fulfill or move will fail
public class FailCondition implements Effect {
    private boolean identifier;
    private int priority;


    public FailCondition(boolean identifier, int priority) {
        this.identifier = identifier;
        this.priority = priority;
    }



    @Override
    public void apply(boolean usedByPlayer1) {
        //No additional effect
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean fails(Round round, boolean movedFirst) {
        Professional user = round.getUser(movedFirst);
        if (priority == 0) { //Charged moves
            Volatile vol = identifier ? CHARGE : DUGIN;
            if (!user.getVolatileStatus().contains(vol)) {
                System.out.println(user.getFullName() + (identifier ? " charged its move." : " dug itself in."));
                user.addVolatileStatus(vol);
                return true;
            }
        } else { //Conditioned moves
            //Identifier indicates the need for the target to attack (if priority is 1 move must attack first)
            //If identifier is false, user cannot be harmed before use
            int[] foeAction = movedFirst ? round.getActionSecond() : round.getActionFirst();
            if (foeAction[0] == 1) {
                //if foe switched, it did not attack causing moves with true identifier to fail and the rest to succeed
                if (identifier) {
                    System.out.println("Move failed because target did not attack this turn!");
                    return true;
                }
            } else {
                Move foeMove = round.getUsedMove(!movedFirst);
                boolean isStatus = foeMove.getType() == "Status";
                if (identifier) { //Foe needs to attack
                    if (isStatus) {
                        //Foe selected status move (did not attack)
                        System.out.println("Move failed because target did not attack this turn!");
                        return true;
                    } else if (priority == 1 && !movedFirst) {
                        //In spite of priority, user attacked second this turn
                        System.out.println(user.getFullName() + " moved too late.");
                        return true;
                    }

                } else if (!isStatus && ((Damaging) foeMove).getDamage(user,round.getUser(!movedFirst)) > 0) {
                    System.out.println("Foe's damage broke " + user.getFullName() + "'s concentration.");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
