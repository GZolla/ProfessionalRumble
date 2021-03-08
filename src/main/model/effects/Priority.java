package model.effects;

import model.Player;
import model.Professional;
import model.Round;
import model.data.Volatile;

import java.util.ArrayList;

import static model.Round.parseIndex;
import static model.moves.Status.PROTECT;

public class Priority implements Effect {
    private int priority;


    public Priority(int priority) {
        this.priority = priority;
    }

    @Override
    //EFFECTS: if priority is 6 user becomes protected, if 3 foe flinches or if 2 user's leader looses 3 critical points
    public void apply(Round round, boolean movedFirst) {
        switch (priority) {
            case 6:
                round.getUser(movedFirst).addVolatileStatus(Volatile.PROTECTED);
                break;
            case 3:
                round.getUser(!movedFirst).addVolatileStatus(Volatile.FLINCH);
                break;
            case 2:
                Player leader = round.getPlayer(movedFirst);
                leader.setCritCounter(leader.getCritCounter() - 3);
                break;
        }
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean fails(Round round, boolean movedFirst) {
        if (priority > 2) {
            ArrayList<Round> battleRounds = round.getBattle().getRounds();
            int roundsSize = battleRounds.size();
            if (roundsSize >= 2) {
                Round lastRound = battleRounds.get(roundsSize - 2);
                boolean wentFirstLastRound = lastRound.wentFirst(round.getPlayer(movedFirst));
                int[] lastRoundAction = lastRound.getAction(wentFirstLastRound);
                int[] lastRoundFoeAction = lastRound.getAction(!wentFirstLastRound);
                boolean wasDefeated = lastRoundFoeAction[0] == 0 && parseIndex(lastRoundFoeAction[1],false) > -1;

                if (lastRoundAction[0] == 0 && !wasDefeated) {
                    Professional user = round.getUser(movedFirst);
                    if (priority == 3) {
                        System.out.println("Move failed, " + user.getFullName() + " already moved after entering.");
                    } else if (user.getMoves()[parseIndex(lastRoundAction[1],true)] == PROTECT) {
                        System.out.println(user.getFullName() + " could not keep protecting itself.");
                    } else {
                        return false;
                    }
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
