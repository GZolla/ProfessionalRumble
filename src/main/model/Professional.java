package model;

import model.data.*;
import model.moves.*;
import model.effects.Counter;

import java.util.ArrayList;

import static model.data.NonVolatile.*;
import static model.data.Volatile.*;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;


//A professional that is part of a team, based on a ProfessionalBase and must have a set of moves
public class Professional {

    private boolean ledByPlayer1;
    private ProfessionalBase base;
    private Move[] moves;
    private int life;

    //stages determine changes in stats:| -6  | -5  | -4  | -3  | -2  | -1  |  0  |  1  |  2  |  3  |  4  |  5  |  6  |
    //                                  | 2/8 | 2/7 | 2/6 | 2/5 | 2/4 | 2/3 | 2/2 | 3/2 | 4/2 | 5/2 | 6/2 | 7/2 | 8/2 |
    private int strengthStage;
    private int resistanceStage;
    private int specialStrengthStage;
    private int specialResistanceStage;
    private int speedStage;

    //Stores statuses and turns since inflicted
    private ArrayList<Volatile> volatileStatus;
    private ArrayList<Integer> volatileTurns;
    private NonVolatile nonVolatileStatus;
    private int nonVolatileTurns;

    private ArrayList<Counter> counters; //current counters


    private int lastMoveUsed; // used in added effects to calculate based on moves data


    //REQUIRES: moves must be 4 long
    //EFFECT: stages start at 0, status and counters as empty
    public Professional(boolean ledByPlayer1, ProfessionalBase base, Move[] moves) {
        this.ledByPlayer1 = ledByPlayer1;
        this.base = base;
        this.moves = moves;
        this.life = base.getLife();
        strengthStage = 0;
        resistanceStage = 0;
        specialStrengthStage = 0;
        specialResistanceStage = 0;
        speedStage = 0;
        volatileStatus = new ArrayList<>();
        counters = new ArrayList<>();
        volatileTurns = new ArrayList<>();

    }

    //--- MOVES --------------------------------------------------------------------------------------------------------

    //EFFECT: returns true if not inflicted with any status that prevents moving
    public boolean canMove() {
        //VS prevents move
        boolean checkVS = volatileStatus.contains(FLINCH) || volatileStatus.contains(DRAIND);
        //NVS prevents move
        boolean checkNVS = nonVolatileStatus == UNEMP || (nonVolatileStatus == DEMOR && nonVolatileTurns % 2 == 1);

        return !(checkNVS || checkVS);
    }

    //EFFECT: returns true and takes damage if nauseated and turns since nauseated are odd
    public boolean gotNauseatedOnMove() {
        boolean gotNOM = volatileStatus.contains(NAUSEA) && getVolatileTurns(NAUSEA) % 2 == 1;
        if (gotNOM) {
            takeDamage(Math.round(10 * base.getStrength() / base.getResistance()));
        }
        return gotNOM;
    }


    //EFFECT: If professional can move and did not get hit with nausea:
    //              Uses the move of given index, changes lastMoveUsed to given index
    //        Updates and checks for status effects
    public void useMove(int i) {
        if (canMove() && !(gotNauseatedOnMove())) {
            lastMoveUsed = i;
            moves[i].use(ledByPlayer1);
        }

        updateVolatileStatus();
        checkNonVolatileStatus();
    }

    //REQUIRES: Last move used is a damaging move and no stat changes to either this or foe occurred since
    //EFFECTS: Return the damage of the last move used
    public int getLastMoveDamage(Professional foe) {
        return ((DamagingMove)moves[lastMoveUsed]).getDamage(this, foe);
    }

    public void setMoves(Move[] moves) {
        this.moves = moves;
    }

    public Move[] getMoves() {
        return moves;
    }

    //--- STATS and STAGES ---------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: set the stage of the corresponding stat to the given stage
    public void setStage(Stat stat, int stage) {
        switch (stat) {
            case STR:
                strengthStage = stage;
                break;
            case RES:
                resistanceStage = stage;
                break;
            case SPS:
                specialStrengthStage = stage;
                break;
            case SPR:
                specialResistanceStage = stage;
                break;
            default:
                speedStage = stage;
                break;
        }
    }


    //EFFECTS: returns the stage of given stat,
    //         ignores increased RES/SPR if foe used critical and decreased STR/SPS if user used critical
    public int getStage(Stat stat, boolean foeUsedCritical) {
        boolean userUsedCritical = volatileStatus.contains(CRITIC);
        switch (stat) {
            case STR:
                return Math.max(strengthStage,userUsedCritical ? 0 : -6);
            case RES:
                return Math.min(resistanceStage,foeUsedCritical ? 0 : 6);
            case SPS:
                return Math.max(specialStrengthStage,userUsedCritical ? 0 : -6);
            case SPR:
                return Math.min(specialResistanceStage,foeUsedCritical ? 0 : 6);
            default:
                return speedStage;
        }
    }

    //EFFECTS: returns the stat of a Professional after the stage multiplier
    //         ignores increased RES/SPR if foe used critical and decreased STR/SPS if user used critical
    public double getRealStat(Stat stat,boolean foeUsedCritical) {
        double baseStat;
        double stage = getStage(stat, foeUsedCritical);
        switch (stat) {
            case STR:
                baseStat = base.getStrength() / (nonVolatileStatus == INJUR ? 2 : 1);
                break;
            case RES:
                baseStat = base.getResistance();
                break;
            case SPS:
                baseStat = base.getSpecialStrengh() / (nonVolatileStatus == DEPRE ? 2 : 1);
                break;
            case SPR:
                baseStat = base.getSpecialResistance();
                break;
            default:
                baseStat = base.getSpeed() / (nonVolatileStatus == DEMOR ? 2 : 1);
                break;
        }

        return baseStat * Math.max(2.0, 2 + stage) / Math.max(2, 2 - stage);
    }

    //--- STATUS -------------------------------------------------------------------------------------------------------

    //MODIFIES:this
    //EFFECTS: add a volatile status to this and add 0 to turns, prevent the addition of duplicates
    public void addVolatileStatus(Volatile status) {
        if (!volatileStatus.contains(status)) {
            volatileStatus.add(status);
            volatileTurns.add(0);
        }
    }

    //MODIFIES:this
    //REQUIRES: status must be in volatileStatus
    //EFFECTS: remove given volatile status from this and its corresponding turns counter
    public void removeVolatileStatus(Volatile status) {
        volatileTurns.remove(volatileStatus.indexOf(status));
        volatileStatus.remove(status);
    }

    //MODFIES: this
    //EFFECTS: add one to every volatileTurn and remove all Statuses that reach its turn limit,
    //         add crit point to leader if not unlucky
    public void updateVolatileStatus() {
        for (int i = 0; i < volatileStatus.size(); i++) {
            int currentValue = volatileTurns.get(i);

            if (currentValue == volatileStatus.get(i).getTurnLimit()) {
                volatileStatus.remove(i);
                volatileTurns.remove(i);
                i--;
            } else {
                volatileTurns.set(i,currentValue + 1);
            }
        }

        if (!volatileStatus.contains(UNLCKY)) {
            Player leader = ledByPlayer1 ? PLAYER_1 : PLAYER_2;
            leader.setCritCounter(Math.min(leader.getCritCounter() + 1,8));
        }
    }

    //MODIFIES: this
    //EFFECT: checks for all the effects of non volatile status(described on class under package data)
    public void checkNonVolatileStatus() {
        if (nonVolatileStatus == SICK) {
            takeDamage(Math.round(base.getLife() * nonVolatileTurns / 16));
        } else if (nonVolatileStatus == INJUR || nonVolatileStatus == DEPRE) {
            takeDamage(Math.round(base.getLife() / 16));
        }

        if (nonVolatileStatus == UNEMP && nonVolatileTurns == 2) {
            nonVolatileStatus = null;
            nonVolatileTurns = 0;
        } else {
            nonVolatileTurns += 1;
        }

    }

    //MODIFIES: this
    //EFFECT: changes nonVolatileStatus unless its fainted
    public void setNonVolatileStatus(NonVolatile nonVolatileStatus) {
        if (nonVolatileStatus != FAINT) {
            this.nonVolatileStatus = nonVolatileStatus;
        }
    }

    public NonVolatile getNonVolatileStatus() {
        return nonVolatileStatus;
    }

    public int getNonVolatileTurns() {
        return nonVolatileTurns;
    }

    public ArrayList<Volatile> getVolatileStatus() {
        return volatileStatus;
    }

    //REQUIRES: status is in volatileStatus
    //EFFECTS: Return turns since inflicted with given status
    public int getVolatileTurns(Volatile status) {
        return volatileTurns.get(volatileStatus.indexOf(status));
    }

    //--- COUNTERS -----------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: add counter to counters
    public void addCounter(Counter counter) {
        counters.add(counter);
    }

    //MODIFIES: this
    //EFFECTS: Updates all counters, if any returns true(has no windowTurns remaining) it is removed from counters
    public void updateCounters() {
        for (int i = 0; i < counters.size(); i++) {
            if (counters.get(i).update()) {
                counters.remove(i);
                i -= 1;
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: use counter of given index, removes it from counters
    public void useCounter(int i) {
        counters.get(i).finalApply(ledByPlayer1);
        counters.remove(i);
    }

    //EFFECTS: returns all counters
    public ArrayList<Counter> getCounters() {
        return counters;
    }

    //EFFECTS: returns all counters with no waitTurns but available windowTurns
    public ArrayList<Counter> getAbleCounters() {
        ArrayList<Counter> ableCounters = new ArrayList<>();
        for (Counter c : counters) {
            if (c.isAble()) {
                ableCounters.add(c);
            }
        }

        return ableCounters;
    }


    //--- OTHER --------------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: foe's life is reduced by given value (negative values mean health recovery)
    public void takeDamage(int amount) {
        life = Math.max(life - amount,0);
        if (life == 0) {
            nonVolatileStatus = FAINT;
        }
    }

    //EFFECTS: checks if the Professional is of the given branch
    public boolean isBranch(Branch branch) {
        int branchIndex = branch.getIndex();
        Branch branch1 = base.getBranch1();
        Branch branch2 = base.getBranch2();

        return (branch1.getIndex() == branchIndex) || (branch2 != null && branch2.getIndex() == branchIndex);
    }

    //EFFECTS: return the effectiveness of a move of given branch against this
    public double checkEffectiveness(Branch branch) {
        int br1Index = base.getBranch1().getIndex();
        double effectiveness = branch.getEffectiveness(br1Index);
        if (base.getBranch2() != null) {
            int br2Index = base.getBranch2().getIndex();
            effectiveness *= branch.getEffectiveness(br2Index);
        }

        return effectiveness;
    }


    //--- ACCESSORS AND MUTATORS ---------------------------------------------------------------------------------------

    public boolean isLedByPlayer1() {
        return ledByPlayer1;
    }

    public String getName() {
        return base.getName();
    }

    public int getLife() {
        return life;
    }

    public int getStrengthStage() {
        return strengthStage;
    }

    public int getResistanceStage() {
        return resistanceStage;
    }

    public int getSpecialStrengthStage() {
        return specialStrengthStage;
    }

    public int getSpecialResistanceStage() {
        return specialResistanceStage;
    }

    public int getSpeedStage() {
        return speedStage;
    }

    public ProfessionalBase getBase() {
        return base;
    }
}
