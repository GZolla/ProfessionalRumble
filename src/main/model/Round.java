package model;

import model.data.ProfessionalBase;
import model.data.Stat;
import model.moves.Damaging;
import model.moves.Move;


//A round of a battle, handles order and holds data for the round
public class Round {
    private Battle battle;
    private Player first;
    private Player second;
    private int[] actionFirst;
    private int[] actionSecond;

    public Round(Battle battle, int[] actionP1, int[] actionP2) {

        this.battle = battle;

        if (determineOrder(actionP1, actionP2)) {
            first = battle.getPlayer1();
            second = battle.getPlayer2();
            actionFirst = actionP1;
            actionSecond = actionP2;
        } else {
            first = battle.getPlayer2();
            second = battle.getPlayer1();
            actionFirst = actionP2;
            actionSecond = actionP1;
        }
    }

    public boolean determineOrder(int[] p1, int[] p2) {
        if (p1[0] + p2[0] > 0) {
            //Either of them switched
            return p1[0] == 1;
            //Since order is irrelevant if both switch, P1 switches first even if P2 is switching as well
        }

        //None switched
        Professional prof1 = battle.getPlayer1().getSelectedProfessional();;
        Professional prof2 = battle.getPlayer1().getSelectedProfessional();
        int prio1 = prof1.getMoves()[p1[1]].getPriority();
        int prio2 = prof2.getMoves()[p2[1]].getPriority();
        if (prio1 == prio2) { //Check priority tie
            double speed1 = prof1.getRealStat(Stat.SPE,false);
            double speed2 = prof2.getRealStat(Stat.SPE,false);
            if (speed1 == speed2) { //check speed tie
                ProfessionalBase b1 = prof1.getBase();
                ProfessionalBase b2 = prof2.getBase();
                if (b1.getSpeed() == b2.getSpeed()) { //check base speed tie
                    return b1.getSpeedDistinct() > b2.getSpeedDistinct();
                }
                return b1.getSpeed() > b2.getSpeed();
            }
            return speed1 > speed2;
        }
        return prio1 > prio2;
    }

    public void handleEffect(Player leader, int[] action) {
        Professional user = leader.getSelectedProfessional();
        if (action[0] == 1) {
            System.out.println(user.getFullName() + " tapped out.");
            Professional newUser = leader.getTeam()[action[1]];
            System.out.println(newUser.getFullName() + " tapped in.");
            leader.setSelectedProfessional(action[1]);
        } else {
            user.useMove(this, action[1]);

        }
        System.out.println("\n");
    }


//--- ACCESSORS & MUTATORS ---------------------------------------------------------------------------------------------

    //EFFECT: Return first if went first, else return second
    public Player getPlayer(boolean wentFirst) {
        return wentFirst ? first : second;
    }

    //EFFECT: Return the selected professional of first(if wentFirst) else of second
    public Professional getUser(boolean wentFirst) {
        return getPlayer(wentFirst).getSelectedProfessional();
    }

    //EFFECT: Get move used (by first if wentFirst else by second)
    public Move getUsedMove(boolean wentFirst) {
        //Assume that this method is only called by effects of moves in this round
        int[] action = wentFirst ? actionFirst : actionSecond;
        if (action[0] == 1) {
            return null;
        } else {
            return getUser(wentFirst).getMoves()[action[1]];
        }
    }

    //EFFECT: Get last move damage
    public int getUsedMoveDamage(boolean wentFirst) {
        Damaging move = (Damaging) getUsedMove(wentFirst);
        //Assume this method is only called when getUsedMove returns a damaging move + no stat changes occurred since

        return move.getDamage(getUser(wentFirst),getUser(!wentFirst));
    }

    public Battle getBattle() {
        return battle;
    }

    public int[] getActionFirst() {
        return actionFirst;
    }

    public int[] getActionSecond() {
        return actionSecond;
    }

    public Player getFirst() {
        return first;
    }

    public Player getSecond() {
        return second;
    }

    public void setActionFirst(int[] actionFirst) {
        this.actionFirst = actionFirst;
    }

    public void setActionSecond(int[] actionSecond) {
        this.actionSecond = actionSecond;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void setFirst(Player first) {
        this.first = first;
    }

    public void setSecond(Player second) {
        this.second = second;
    }
}
