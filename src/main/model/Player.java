package model;

import model.data.Volatile;

import static model.data.Volatile.CRITIC;

//Class represents the players, handles team management and critical attacks
public class Player {

    private String name;
    private int critPoints;
    private Professional[] team;
    private int selectedProfessional;


    //EFFECT: create new player, starts with the first professional of the team and no critical points
    public Player(String name) {
        this.name = name;
        critPoints = 0;
        selectedProfessional = 0;
    }

    //--- BUILD TEAM ---------------------------------------------------------------------------------------------------


    public void setTeam(Professional[] team) {
        this.team = team;
    }

    public Professional[] getTeam() {
        return team;
    }

    //--- CRITICAL POINTS ----------------------------------------------------------------------------------------------

    //REQUIRES: critPoints are in [-1,8] (-1 only before adding additional crit points at end of turn)
    public void setCritCounter(int critPoints) {
        this.critPoints = critPoints;
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

    public Professional getSelectedProfessional() {
        return team[selectedProfessional];
    }

    public int getCritCounter() {
        return critPoints;
    }
}
