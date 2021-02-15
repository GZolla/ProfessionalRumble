package model;

import model.data.NonVolatile;
import model.data.Volatile;
import model.moves.Move;

import static model.data.ProfessionalBase.*;
import static model.data.ProfessionalBase.QUARTERBACK;
import static model.data.Volatile.CRITIC;
import static model.moves.DamagingMove.*;
import static model.moves.DamagingMove.MOMENTUM_COLLISION;
import static model.moves.StatusMove.*;
import static model.moves.StatusMove.ACCELERATE;

//Class represents the players, handles team management and critical attacks
public class Player {

    private String name;
    private int critPoints;
    private Professional[] team;
    private int selectedProfessional;


    //EFFECT: create new player, starts with the first professional of the team and no critical points
    //        sets a default team each with their default move set.
    public Player(String name, boolean isPLayer1) {
        this.name = name;
        critPoints = 0;
        selectedProfessional = 0;
        Move[] compSciMoves = new Move[]{CABLE_WHIP,COMPROMISE_NETWORK,KEYBOARD_SLAM,WORKOUT};
        Move[] presiMoves = new Move[]{PRESI_PUNCH,ICBM_STRIKE,SURVEILL,OPTIMIZATION};
        Move[] cbcMoves = new Move[]{REITERATIVE_PUNCH,ICBM_STRIKE,SURVEILL,OPTIMIZATION};
        Move[] coachMoves = new Move[]{BACKPACK_SMACK,DISOBEDIENCE_SLAP, DOMINATE,REDCARD_FOUL};
        Move[] dictMoves = new Move[]{ICBM_STRIKE,HYPEREXAMINE,LONG_AWAITED_NOVEL,CORPORAL_PUNISHMENT};
        Move[] qbMoves = new Move[]{WORKOUT,ACCELERATE,FRIENDLY_MATCH,MOMENTUM_COLLISION};

        Professional compSci = new Professional(isPLayer1, COMPUTER_SCIENTIST,compSciMoves);
        Professional president = new Professional(isPLayer1, PRESIDENT,presiMoves);
        Professional centralBankChair = new Professional(isPLayer1, CENTRAL_BANK_CHAIR,cbcMoves);
        Professional selfDefenseCoach = new Professional(isPLayer1, SELFDEFENSE_COACH,coachMoves);
        Professional dictator = new Professional(isPLayer1,DICTATOR,dictMoves);
        Professional quarterback = new Professional(isPLayer1, QUARTERBACK,qbMoves);
        team = new Professional[]{compSci,president,centralBankChair,selfDefenseCoach,dictator,quarterback};
    }

    //--- BUILD TEAM ---------------------------------------------------------------------------------------------------

    public String[] getTeamNames() {
        String[] names = new String[team.length];
        for (int i = 0; i < team.length; i++) {
            names[i] = team[i].getName();
        }
        return names;
    }

    public void setTeam(Professional[] team) {
        this.team = team;
    }

    public Professional[] getTeam() {
        return team;
    }

    public String[][] getTeamTable() {
        String[][] table = new String[team.length][4];
        for (int i = 0; i < team.length; i++) {
            table[i][0] = i + "";
            table[i][1] = team[i].getName();
            table[i][2] = team[i].getLife() + " / " + team[i].getBase().getLife();
            NonVolatile status = team[i].getNonVolatileStatus();
            if (status == null) {
                table[i][3] = "-";
            } else {
                table[i][3] = status.getName();
            }
        }
        return table;
    }

    //--- CRITICAL POINTS ----------------------------------------------------------------------------------------------

    //REQUIRES: critPoints are in [-1,8] (-1 only before adding additional crit points at end of turn)
    public void setCritCounter(int critPoints) {
        if (critPoints != this.critPoints) {
            String change = critPoints > this.critPoints ? "increased" : "decreased";
            System.out.println(name + "'s critical points " + change + " to " + critPoints);
            this.critPoints = critPoints;
        }

    }

    //EFFECT: checks that the selected professional can move and crit points are full
    public boolean canUseCritical() {
        return (critPoints == 8) && team[selectedProfessional].canMove();
    }

    //EFFECTS: sets Professional usedCritical to true and empties critPoints
    public void useCritical() {
        team[selectedProfessional].addVolatileStatus(CRITIC);
        critPoints = 0;
    }

    //--- ACCESSORS AND MUTATORS ---------------------------------------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public void setSelectedProfessional(int selectedProfessional) {
        this.selectedProfessional = selectedProfessional;
    }

    public String getName() {
        return name;
    }

    public int getSelectedIndex() {
        return selectedProfessional;
    }

    public Professional getSelectedProfessional() {
        return team[selectedProfessional];
    }

    public int getCritCounter() {
        return critPoints;
    }
}
