package model;

import model.data.Stat;
import model.data.Volatile;
import model.moves.Damaging;
import model.moves.Move;
import persistence.Writable;
import org.json.JSONObject;

import static model.data.NonVolatile.*;

import static model.moves.Damaging.LANDMINE;
import static ui.BattleManager.handleWage;
import static ui.BattleManager.selectProfessional;
import static ui.UiManager.largeOptions;


//A round of a battle, handles order and holds data for the round
public class Round implements Writable {
    private Battle battle;
    private Player first;
    private Player second;
    private int[] actionFirst;
    private int[] actionSecond;


    //EFFECT: Creates a round from the actions taken by both players, decides who goes first based on determineOrder
    public Round(Battle battle, int[] actionP1, int[] actionP2) {

        this.battle = battle;


        first = battle.getPlayer1();
        second = battle.getPlayer2();
        actionFirst = actionP1;
        actionSecond = actionP2;
    }

    //MODIFIES: this
    //EFFECT: Returns true if player 1 is moving first based on the following criteria:
    //            - If any user switches it moves first (if both switch, player 1 moves first)
    //            - Otherwise highest priority move moves first
    //            - If equal in priority, fastest professional moves first
    //            - If a tie still occurs then call openWager to handle it
    private boolean determineOrder(int[] p1, int[] p2) {
        if (p1[0] % 3 + p2[0] % 3 > 0) {
            //Either of them switched
            return p1[0] % 3 == 1;
            //Since order is irrelevant if both switch, P1 switches first even if P2 is switching as well
        } else {
            Professional prof1 = battle.getPlayer1().getSelectedProfessional();
            Professional prof2 = battle.getPlayer2().getSelectedProfessional();

            Move move1 = prof1.getMoves()[parseIndex(p1[1],true)];
            Move move2 = prof2.getMoves()[parseIndex(p2[1],true)];

            if (move1 == LANDMINE || move2 == LANDMINE) {
                return move2 == LANDMINE; //if p2 used landmine: p1 foes first, otherwise return false: p2 goes first
            }
            int priorityDifference = move1.getPriority() - move2.getPriority();
            if (priorityDifference == 0) {
                double speDiff = prof1.getRealStat(Stat.SPE,false) - prof2.getRealStat(Stat.SPE,false);
                if (speDiff == 0) {
                    return openWager((int) Math.floor(p1[0] / 3), (int) Math.floor(p2[0] / 3));
                } else {
                    return speDiff > 0;
                }
            } else {
                return priorityDifference > 0;
            }
        }
    }

    //MODIFIES:this
    //EFFECTS: Reorders first and second, and firstAction and secondAction if determine order returns false
    public void reorder() {
        if (!determineOrder(actionFirst, actionSecond)) {
            first = battle.getPlayer2();
            second = battle.getPlayer1();
            int[] temp = actionFirst;
            actionFirst = actionSecond;
            actionSecond = temp;
        }
    }

    
    //MODIFIES: this
    //EFFECT: Asks each player to wager a number of critical points,
    //        returns true if player 1 wages more, if equal round becomes simultaneous
    private boolean openWager(int storedWage1, int storedWage2) {



        int wager1;
        if (storedWage1 > 0) {
            wager1 = storedWage1 - 1;
        } else {
            wager1  = handleWage(getPlayer(true));
            actionFirst[0] += wager1;
        }
        int wager2;
        if (storedWage2 > 0) {
            wager2 = storedWage1 - 1;
        } else {
            wager2 = handleWage(getPlayer(true));
            actionSecond[0] += wager1;
        }

        if (wager1 == wager2) {
            first.getSelectedProfessional().addVolatileStatus(Volatile.FLINCH);
            second.getSelectedProfessional().addVolatileStatus(Volatile.FLINCH);
        }
        return  wager1 >= wager2;
    }






    public int handleAll() {
        System.out.println("--------------------------------------------------------");
        reorder();
        boolean p1WentFirst = wentFirst(battle.getPlayer1());
        boolean secondFainted = handleAction(true);
        int result = -1;

        if (!secondFainted) {
            handleAction(false);
            if (getUser(true).getNonVolatileStatus() == FAINT) {
                result = p1WentFirst ? 1 : 0;//second did not faint and p1 was defeated
            } else {
                first.getSelectedProfessional().checkEndTurn();
            }
            second.getSelectedProfessional().checkEndTurn();
        } else {
            first.getSelectedProfessional().checkEndTurn();
        }

        if (getUser(false).getNonVolatileStatus() == FAINT) {
            result = p1WentFirst ? 0 : 1; //player 2 was defeated
        }
        System.out.println(getUser(true).getFullName() + ": " + getUser(true).getLife());
        System.out.println(getUser(false).getFullName() + ": " + getUser(false).getLife());
        battle.setResult(result);
        return result;//neither player was defeated
    }

    public boolean handleAction(boolean handlingFirst) {


        Professional user = getUser(handlingFirst);
        int[] action = handlingFirst ? actionFirst : actionSecond;
        boolean result = false;
        if (action[0] == 1) {
            System.out.println(user.getFullName() + " tapped out.");
            user.tapOut(action[1]);
        } else {
            user.useMove(this,handlingFirst, parseIndex(action[1],true)); //*
            result = handleFaint(handlingFirst,parseIndex(action[1],false));

        }
        System.out.println("\n");
        return result;


    }


    public boolean handleFaint(boolean first, int newProfInd) {
        Professional opponent = getUser(!first);
        if (opponent.getNonVolatileStatus() == FAINT) {
            if (newProfInd < -1) {
                newProfInd = selectProfessional(getPlayer(!first));
                int oldIndex = (first ? actionFirst : actionSecond)[1];
                (first ? actionFirst : actionSecond)[1] = parseIndex(oldIndex,true) + 4 * (newProfInd + 2);
            }
            if (newProfInd != -1) {
                opponent.tapOut(newProfInd);
            }
            return true;
        }
        return false;
    }

    //EFFECTS: parses the move index:
    //             - if under or equal to -2, add 3 and then multiply by -1.
    //             - Afterwards the remainder of the index divided by 4 is the index of the move used
    //             - (Integer division by 4) - 2 represents index of the prof. tapping in(or -1 if all fainted)
    //         return move index if moveOrProf is true, otherwise return prof. index
    public static int parseIndex(int index, boolean moveOrProf) {
        if (index <= -2) {
            index = (index + 3) * -1;
        }
        return moveOrProf ? index % 4 : (index / 4) - 2;
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

    //EFFECT: Get move used (by first if wentFirst else by second), returns null if professional tapped out instead
    public Move getUsedMove(boolean wentFirst) {
        //Assume that this method is only called by effects of moves in this round
        int[] action = wentFirst ? actionFirst : actionSecond;
        if (action[0] == 1) {
            return null;
        } else {
            return getUser(wentFirst).getMoves()[parseIndex(action[1],true)];
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

    public Player getFirst() {
        return first;
    }

    public Player getSecond() {
        return second;
    }

    public int[] getAction(boolean isFirst) {
        return isFirst ? actionFirst : actionSecond;
    }


    public void setActionFirst(int[] actionFirst) {
        this.actionFirst = actionFirst;
    }

    public void setActionSecond(int[] actionSecond) {
        this.actionSecond = actionSecond;
    }


    public boolean wentFirst(Player p) {
        return first.getId() == p.getId();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();



        int[][] actions;
        if (wentFirst(battle.getPlayer1())) {
            actions = new int[][]{actionFirst,actionSecond};
        } else {
            actions = new int[][]{actionSecond,actionFirst};
        }
        for (int i = 0; i < actions.length; i++) {
            json.put("P" + i + "action",actions[i][0]);
            json.put("P" + i + "index",actions[i][1]);
        }

        return json;
    }

    @Override
    public String getName() {
        return null;
    }
}
