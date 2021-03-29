package model;

import model.exceptions.EssentialFileFailed;
import model.data.NonVolatile;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.SaveAble;
import persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.Main;
import ui.UserManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import static model.data.NonVolatile.FAINT;
import static model.data.Volatile.CRITIC;
import static model.data.Volatile.UNLCKY;
import static model.effects.CriticalModifier.MAXCRITS;

//Class represents the players, handles team management and critical attacks
public class Player implements Writable {


    private final int id;
    private String name;


    private int criticalPoints;
    private Team team;
    private int selectedProfessional;


    public Player(int id, String name) {
        this.id = id;
        this.name = name;

    }

    //EFFECTS: Construct player from stored data, if user does not exist name is "Guest"
    public Player(JSONObject data) {
        this.id = data.getInt("id");
        this.team = new Team(this, data.getJSONObject("team"));
        this.criticalPoints = 0;
        this.selectedProfessional = 0;

        String name = UserManager.findName(this.id);
        this.name = name == null ? "Guest" : name;
    }

    //EFFECTS: load all items in either the battle's or team's file for this user
    public SaveAble[] loadAll(String type) {
        try {
            JSONArray data = new JsonReader(type + "/" + id + ".json").arrayRead();
            SaveAble[] items = new SaveAble[data.length()];
            for (int i = 0; i < data.length(); i++) {
                if (type.equals("battles")) {
                    items[i] = new Battle(i, data.getJSONObject(i));
                } else {
                    items[i] = new Team(this,data.getJSONObject(i));
                }
            }
            return items;
        } catch (IOException e) {
            throw new EssentialFileFailed();
        }
    }


//--- BUILD TEAM ---------------------------------------------------------------------------------------------------

    //EFFECTS: returns all the teams stored for this user
    public SaveAble[] loadTeams() {
        return loadAll("teams");
    }


    //EFFECTS: remove team with given index from file
    public void removeTeam(int index) {
        SaveAble[] teams = loadTeams();
        JSONArray jsonTeams = new JSONArray();
        for (SaveAble t : teams) {
            jsonTeams.put(t.toJson());
        }
        jsonTeams.remove(index);

        JsonWriter writer = new JsonWriter("/teams/" + id + ".json");
        try {
            writer.open();
            writer.writeArray(jsonTeams);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new EssentialFileFailed();
        }

    }

    public Professional[] getTeamMembers() {
        return team.getMembers();
    }

    public String[][] getTeamTable() {
        Professional[] members = team.getMembers();
        String[][] table = new String[members.length][4];
        for (int i = 0; i < members.length; i++) {
            table[i][0] = i + "";
            table[i][1] = members[i].getName();
            table[i][2] = members[i].getLife() + " / " + members[i].getBase().getLife();
            NonVolatile status = members[i].getNonVolatileStatus();
            if (status == null) {
                table[i][3] = "-";
            } else {
                table[i][3] = status.getName();
            }
        }
        return table;
    }

    @Override
    public JSONObject toJson() {
        JSONObject user = new JSONObject();

        if (team != null) {
            user.put("id",id);
            user.put("team",team.toJson());
        } else {
            user.put("name",name);
        }

        return user;
    }

//--- BATTLE TOOLS -------------------------------------------------------------------------------------------------

    //EFFECTS: returns all the battles stored for the user
    public SaveAble[] loadBattles() {
        return loadAll("battles");
    }


    //MODIFIES: this
    //EFFECTS: checks selected is not = to teams length, (returning false if it is) and setting
    //         this.team to team with index 'selected' in teams, and both criticalPoints and selectedProfessional to 0
    public boolean setBattleProperties(int selected, SaveAble[] teams) {
        if (selected == teams.length) {
            return false;
        }
        team = new Team(this, teams[selected].toJson());//Ensure team is saved with this as leader
        criticalPoints = 0;
        selectedProfessional = 0;
        return true;
    }

    //MODIFIES: this
    //EFFECTS: sets team to given one, and both criticalPoints and selectedProfessional to 0
    public boolean setBattleProperties(Team team) {
        this.team = new Team(this, team.toJson());//Ensure team is saved with this as leader
        criticalPoints = 0;
        selectedProfessional = 0;
        return true;
    }


    //EFFECTS: Checks if it has selected a team to battle
    public boolean isReady() {
        return team != null;
    }

    //MODIFIES: this
    //EFFECTS: makes team null
    public void unready() {
        team = null;
    }

//--- CRITICAL POINTS ----------------------------------------------------------------------------------------------

    //EFFECT: sets critPoints to given points, if outside [0,MAXCRITS] it corrects points to max/min accordingly
    public void setCritCounter(int critPoints) {
        critPoints = Math.max(0,Math.min(MAXCRITS,critPoints)); //Forces points into [0,MAXCRITS]
        if (critPoints != this.criticalPoints) {
            String change = critPoints > this.criticalPoints ? "increased" : "decreased";
            Main.BATTLEMGR.log(name + "'s critical points " + change + " to " + critPoints);
            this.criticalPoints = critPoints;
        }

    }

    //EFFECT: checks that the selected professional can move and crit points are full
    public boolean canUseCritical() {
        return (criticalPoints >= (3 * MAXCRITS) / 4) && team.getMembers()[selectedProfessional].canMove();
    }

    //EFFECTS: sets Professional usedCritical to true and empties critPoints
    public void useCritical() {
        team.getMembers()[selectedProfessional].addVolatileStatus(CRITIC);
        criticalPoints = 0;
    }

    //EFFECTS: Runs when selected professional used damaging move, checks if selected professional is UNLUCKY,
    //         and if not, adds one to critical point
    public void raiseCriticals() {
        Professional selected = team.getMembers()[selectedProfessional];
        if (!selected.hasVolatileStatus(UNLCKY)) {
            setCritCounter(criticalPoints + MAXCRITS / 8);
        } else {
            String prompt = selected.getName() + "'s unluckiness  prevented ";
            Main.BATTLEMGR.log(prompt + name + " from gaining critical points.");
        }
    }

//--- PERSISTENCE ------------------------------------------------------------------------------------------------------
    public void saveToUsers(String newPassword, String newSalt) {
        try {
            JSONArray users = new JsonReader("users.json").arrayRead();
            JSONObject userData;
            if (newPassword == null) {
                userData =  users.getJSONObject(id);
                userData.put("name",name);
            } else {
                userData = toJson();
                userData.put("password",newPassword);
                userData.put("salt",newSalt);
            }
            users.put(id,userData);

            JsonWriter writer = new JsonWriter("users.json");
            writer.open();
            writer.writeArray(users);
            writer.close();
        } catch (IOException e) {
            throw new EssentialFileFailed();
        }

    }

//--- ACCESSORS AND MUTATORS ---------------------------------------------------------------------------------------


    public int getId() {
        return id;
    }

    //MODIFIES: this
    //EFFECTS: Changes name to given name, changes the stored value as well.
    public void setName(String name) {
        this.name = name;
        saveToUsers(null,null);
    }


    public String getName() {
        return name;
    }

    public Professional getSelectedProfessional() {
        return team.getMembers()[selectedProfessional];
    }

    public int getCritCounter() {
        return criticalPoints;
    }

    public Team getTeam() {
        return team;
    }

    public void setSelectedProfessional(int selectedProfessional) {
        this.selectedProfessional = selectedProfessional;
    }

    public int getSelectedProfessionalIndex() {
        return selectedProfessional;
    }

    //EFFECTS: Checks if there are non-fainted professionals in team other than the selected professional
    public boolean hasAbleReplacements() {
        Professional[] members = team.getMembers();
        for (int i = 0; i < members.length; i++) {
            if (members[i].getNonVolatileStatus() != FAINT && i != selectedProfessional) {
                return true;
            }
        }
        return false;
    }


}
