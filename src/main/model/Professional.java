package model;

import model.data.*;
import model.moves.*;
import model.effects.CounterSetter;
import model.moves.Status;
import persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static model.data.NonVolatile.*;
import static model.data.Volatile.*;


//A professional that is part of a team, based on a ProfessionalBase and must have a set of moves
public class Professional implements Writable {

    //Default properties before any battle,
    private final Team team;
    private ProfessionalBase base;
    private final Move[] moves;


    private int life;

    //stages determine changes in stats:| -6  | -5  | -4  | -3  | -2  | -1  |  0  |  1  |  2  |  3  |  4  |  5  |  6  |
    //                                  | 2/8 | 2/7 | 2/6 | 2/5 | 2/4 | 2/3 | 2/2 | 3/2 | 4/2 | 5/2 | 6/2 | 7/2 | 8/2 |
    private int strengthStage;
    private int resistanceStage;
    private int specialStrengthStage;
    private int specialResistanceStage;
    private int speedStage;

    //Stores statuses and turns since inflicted
    private final ArrayList<Volatile> volatileStatus;
    private final ArrayList<Integer> volatileTurns;
    private NonVolatile nonVolatileStatus;
    private int nonVolatileTurns;


    private final int[] moveCounters; //current counters



    //REQUIRES: moves must be 4 long
    //EFFECT: stages start at 0, status and counters as empty, moveCounters filled with -1
    public Professional(Team team, ProfessionalBase base, Move[] moves) {
        this.team = team;
        this.base = base;
        this.moves = moves;
        this.life = base.getLife();
        strengthStage = 0;
        resistanceStage = 0;
        specialStrengthStage = 0;
        specialResistanceStage = 0;
        speedStage = 0;
        volatileStatus = new ArrayList<>();
        volatileTurns = new ArrayList<>();
        moveCounters = new int[4];
        for (int i = 0; i < 4; i++) {
            moveCounters[i] = 0;
        }

    }

    //EFFECT: Constructs a professional from a JSONObject
    public Professional(Team team, JSONObject json) {
        this.team = team;
        this.base = ProfessionalBase.values()[json.getInt("base")];
        this.moves = movesFromJson(json.getJSONArray("moves"));


        this.life = base.getLife();
        strengthStage = 0;
        resistanceStage = 0;
        specialStrengthStage = 0;
        specialResistanceStage = 0;
        speedStage = 0;
        volatileStatus = new ArrayList<>();
        volatileTurns = new ArrayList<>();
        moveCounters = new int[4];
        for (int i = 0; i < 4; i++) {
            moveCounters[i] = 0;
        }

    }

//--- MOVES ------------------------------------------------------------------------------------------------------------

    //EFFECT: returns true if not inflicted with any status that prevents moving
    public boolean canMove() {
        //VS prevents move
        boolean checkVS = false;
        if (hasVolatileStatus(FLINCH)) {
            System.out.println(getFullName() + " flinched.");
        } else if (hasVolatileStatus(DRAIND)) {
            System.out.println(getFullName() + " is still drained from last move.");
        } else {
            checkVS = true;
        }
        //NVS prevents move
        boolean checkNVS = false;
        if (nonVolatileStatus == UNEMP) {
            System.out.println(getFullName() + " is unemployed and cannot move.");
        } else if (nonVolatileStatus == DEMOR && nonVolatileTurns % 2 == 1) {
            System.out.println(getFullName() + " is too demoralised to act this turn.");
        } else {
            checkNVS = true;
        }

        return checkNVS && checkVS;
    }

    //MODIFIES: this
    //EFFECT: returns true and takes damage if nauseated and turns since nauseated are odd
    public boolean gotNauseatedOnMove() {
        boolean gotNOM = volatileStatus.contains(NAUSEA) && getVolatileTurns(NAUSEA) % 2 == 1;
        if (gotNOM) {
            System.out.println(getFullName() + " was hit with nausea.");
            takeDamage((10 * base.getStrength()) / base.getResistance(),1);
        }
        return gotNOM;
    }

    //EFFECT: checks if this has been assigned given move
    public boolean hasMove(Move move) {
        for (Move m : moves) {
            if (m == move) {
                return true;
            }
        }
        return false;
    }

    //EFFECT: If professional can move and did not get hit with nausea:
    //              Uses the move of given index, changes lastMoveUsed to given index
    public void useMove(Round round, boolean movesFirst, int i) {
        if (!gotNauseatedOnMove() && canMove()) {
            System.out.println(getFullName() + " used " + moves[i].getName() + ".");
            moves[i].use(round, movesFirst);
        }
        updateCounters(i);
    }

    //MODIFIES: this
    //EFFECT: Checks Statuses calling the method that handles each
    public void checkEndTurn() {
        updateVolatileStatus();
        checkNonVolatileStatus();

    }


//--- STATS and STAGES -------------------------------------------------------------------------------------------------

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
                baseStat = base.getStrength() / (nonVolatileStatus == INJUR ? 2.0 : 1.0);
                break;
            case RES:
                baseStat = base.getResistance();
                break;
            case SPS:
                baseStat = base.getSpecialStrentgh() / (nonVolatileStatus == DEPRE ? 2.0 : 1.0);
                break;
            case SPR:
                baseStat = base.getSpecialResistance();
                break;
            default:
                baseStat = base.getSpeed() / (nonVolatileStatus == DEMOR ? 2.0 : 1.0);
                break;
        }

        return baseStat * Math.max(2.0, 2 + stage) / Math.max(2, 2 - stage);
    }

//--- STATUS -----------------------------------------------------------------------------------------------------------

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

    public boolean hasVolatileStatus(Volatile status) {
        return volatileStatus.contains(status);
    }

    //MODFIES: this
    //EFFECTS: add one to every volatileTurn and remove all Statuses that reach its turn limit
    public void updateVolatileStatus() {
        for (int i = 0; i < volatileStatus.size(); i++) {
            int currentValue = volatileTurns.get(i);
            Volatile currentStatus = volatileStatus.get(i);
            if (currentValue == currentStatus.getTurnLimit()) {
                System.out.println(getFullName() + " is no longer " + currentStatus.getName() + ".");
                volatileStatus.remove(i);
                volatileTurns.remove(i);
                i--;
            } else {
                volatileTurns.set(i,currentValue + 1);
            }
        }
    }


    //MODIFIES: this
    //EFFECT: changes nonVolatileStatus unless its fainted
    public void setNonVolatileStatus(NonVolatile nonVolatileStatus) {
        if (this.nonVolatileStatus != FAINT) {
            this.nonVolatileStatus = nonVolatileStatus;
            this.nonVolatileTurns = 0;
        }
    }

    //MODIFIES: this
    //EFFECT: checks for all the effects of non volatile status(described on class under package data)
    public void checkNonVolatileStatus() {
        if (nonVolatileStatus == SICK) {
            takeDamage((base.getLife() * (nonVolatileTurns + 1)) / 16,1);
        } else if (nonVolatileStatus == INJUR || nonVolatileStatus == DEPRE) {
            takeDamage(base.getLife() / 16,1);
        }

        if (nonVolatileStatus == UNEMP && nonVolatileTurns == 2) {
            nonVolatileStatus = null;
            nonVolatileTurns = 0;
        } else {
            nonVolatileTurns += 1;
        }

    }

    //REQUIRES: status is in volatileStatus
    //EFFECTS: Return turns since inflicted with given status
    public int getVolatileTurns(Volatile status) {
        return volatileTurns.get(volatileStatus.indexOf(status));
    }

//--- COUNTERS ---------------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: add counter to counters
    public void setMoveCounter(int index, int val) {
        moveCounters[index] = val;
    }

    //MODIFIES: this
    //EFFECTS: Checks all counters, if any is in its use window then reduce the counter by 1
    public void updateCounters(int usedMoveIndex) {
        for (int i = 0; i < 4; i++) {

            if (moveCounters[i] > 0 && i != usedMoveIndex) {  //Move counter is set and move was not used this turn
                //if move is not set then there is no need to reduce counter
                //if used this turn, it means it was charged/applied so no reduction to window takes place
                CounterSetter effect = (CounterSetter) moves[i].getEffect();
                if (effect.getWindowTurns() >= moveCounters[i]) {
                    moveCounters[i] -= 1;
                }

            }
        }

    }

    public int[] getMoveCounters() {
        return moveCounters;
    }


//--- OTHER --------------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: foe's life is reduced by given value (negative values mean health recovery)
    public void takeDamage(int amount, double effectiveness) {
        int realDmg = Math.min(life,Math.max(life - base.getLife(),amount));
        System.out.println(getFullName() + (realDmg > 0 ? " lost " : " gained ") + realDmg + " life points.");
        life -= realDmg;
        if (effectiveness == 0) {
            System.out.println(getFullName() + " was not affected.");
        } else if (effectiveness < 1) {
            System.out.println("It was a weak move.");
        } else if (effectiveness > 1) {
            System.out.println("It was super effective!");
        }

        if (life == 0) {
            System.out.println(getFullName() + " fainted.");
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

    //EFFECTS: Removes all Volatile status, reset all move counters
    public void tapOut(int replacement) {
        volatileStatus.clear();
        strengthStage = 0;
        resistanceStage = 0;
        specialStrengthStage = 0;
        specialResistanceStage = 0;
        speedStage = 0;

        Professional newUser = team.getLeader().getTeamMembers()[replacement];
        System.out.println(newUser.getFullName() + " tapped in.");
        team.getLeader().setSelectedProfessional(replacement);
    }

    //EFFECT: Get name with leader identifier.
    public String getFullName() {
        return team.getLeader().getName() + "'s " + getName();
    }



    @Override
    //EFFECT: returns this object as JSON string, including the index of its base and each move
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("base", base.getIndex());
        json.put("moves", movesToJson());
        return json;
    }

    //EFFECT: Returns the index of the moves in a JSONArray
    private JSONArray movesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Move m : moves) {
            JSONObject json = new JSONObject();
            json.put("status?", m instanceof Status);
            json.put("index", m.getIndex());
            jsonArray.put(json);
        }

        return jsonArray;
    }

    public Move[] movesFromJson(JSONArray jsonArray) {
        Move[] moves = new Move[4];
        for (int i = 0; i < jsonArray.length(); i++) {
            //Retrieve JSONObject from JSONArray
            JSONObject jsonMove = (JSONObject) jsonArray.get(i);
            //Build Professional
            Move newMove;
            if (jsonMove.getBoolean("status?")) {
                newMove = model.moves.Status.values()[jsonMove.getInt("index")];
            } else {
                newMove = Damaging.values()[jsonMove.getInt("index")];
            }
            //Add Professional
            moves[i] = newMove;
        }

        return moves;
    }


//--- ACCESSORS AND MUTATORS ---------------------------------------------------------------------------------------

    //EFFECTS: retuns a String array with the names of all this professional's moves
    public String[] getMoveNames() {
        String[] names = new String[moves.length];
        for (int i = 0; i < moves.length; i++) {
            names[i] = moves[i].getName();
        }
        return names;
    }

    public NonVolatile getNonVolatileStatus() {
        return nonVolatileStatus;
    }

    public Player getLeader() {
        return team.getLeader();
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



    //EFFECT: Changes base to new base and life to the base's life
    public void setBase(ProfessionalBase base) {
        this.base = base;
        this.life = base.getLife();
    }

    //EFFECTS: sets move of given index to new move
    public void setMove(int index, Move move) {
        this.moves[index] = move;
    }

    public Move[] getMoves() {
        return moves;
    }



}
