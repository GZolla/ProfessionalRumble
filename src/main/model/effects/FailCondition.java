package model.effects;

import model.Professional;
import model.Round;
import model.moves.Move;
import model.moves.Status;


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
    public void apply(Round round, boolean movedFirst) {
        //No additional effect
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    //EFFECT: Checks if move has failed according to following criteria and prints corresponding message:
    //         - If priority is 0 move needs charging / digging in, fails if professional was not
    //              - If identifier is true, needs charging, otherwise needs digging in
    //              - Professional charges / digs in
    //         - Otherwise move is conditioned by foe's selected move
    //              - If identifier it needs the target to attack (if priority is 1 user must also attack first)
    //              - Other wise, user cannot have been damaged before using this move
    public boolean fails(Round round, boolean movedFirst) {

        Professional user = round.getUser(movedFirst);

        String message = "";
        Move foeMove = round.getUsedMove(!movedFirst);
        boolean usedDamaging = foeMove != null && !(foeMove instanceof Status);
        if (priority == 0) {
            if (!user.hasVolatileStatus(identifier ? CHARGE : DUGIN)) {
                message = user.getFullName() + (identifier ? " charged its move." : " dug itself in.");
                user.addVolatileStatus(identifier ? CHARGE : DUGIN);
            }
        } else if (identifier) {
            if (!usedDamaging) {
                message = "Move failed because target did not attack this turn!";
            } else if (priority == 1 && !movedFirst) {
                //In spite of priority, user attacked second this turn
                message = user.getFullName() + " moved too late.";
            }
        } else if (!movedFirst && usedDamaging && round.getUsedMoveDamage(true) > 0) {
            message = "Foe's damage broke " + user.getFullName() + "'s concentration.";
        }
        if (message != "") {
            System.out.println(message);
            return true;
        }
        return false;
    }

    @Override
    //EFFECTS: based on the effect described above, return accurate description of move
    public String getDescription() {
        if (priority == 0) {
            //2-turn moves, charge if identifier, dug in otherwise
            return identifier ? "Move needs charging before use." : "Professional digs in before attacking.";
        } else if (identifier) {
            //Conditioned to target attacking, if priority == 1 user must attack first
            return "Move fails if foe doesn't attack" +  (priority == 1 ? " or if it moves first" : "") + ".";
        } else {
            //User cannot be harmed
            return "Move fails if user is damaged before attacking.";
        }
    }

}
