package model;

import model.moves.Move;
import persistence.SaveAble;
import org.json.JSONArray;
import org.json.JSONObject;


import static model.data.ProfessionalBase.*;
import static model.data.ProfessionalBase.QUARTERBACK;
import static model.moves.Damaging.*;
import static model.moves.Damaging.MOMENTUM_COLLISION;
import static model.moves.NonDamaging.*;
import static model.moves.NonDamaging.ACCELERATE;

public class Team implements SaveAble {

    private final Player leader;
    private String name;
    private Professional[] members;


    //EFFECTS: Creates team with given name and default members
    public Team(Player leader, String name) {
        this.leader = leader;
        this.name = name;

        Move[] compSciMoves = new Move[]{BREAKTHROUGH_BLAST,COMPROMISE_NETWORK,KEYBOARD_SLAM,SLAPSTICK_SMACK};
        Move[] presiMoves = new Move[]{PRESIDENTIAL_PUNCH,INSIDER_TRADING, BAD_INFLUENCE,PAPER_SLASH};
        Move[] envMoves = new Move[]{REITERATIVE_PUNCH,UNDERGROUND_UPPERCUT, HARVEST_HAVOC,OPTIMIZATION};
        Move[] coachMoves = new Move[]{LIGHTSPE_KICK,GRAVITY_SLAM, DOMINATE,RED_CARD_FOUL};
        Move[] dictMoves = new Move[]{POP_UP_PUNCH,HYPEREXAMINE,LANDMINE,PROTECT};
        Move[] qbMoves = new Move[]{PSYCHOLOGICAL_TORTURE,ACCELERATE,FRIENDLY_MATCH,MOMENTUM_COLLISION};

        Professional compSci = new Professional(this, COMPUTER_SCIENTIST,compSciMoves);
        Professional president = new Professional(this, PRESIDENT,presiMoves);
        Professional cbChair = new Professional(this, ENVIRONMENTALIST,envMoves);
        Professional sdCoach = new Professional(this, SELFDEFENSE_COACH,coachMoves);
        Professional dictator = new Professional(this,DICTATOR,dictMoves);
        Professional quarterback = new Professional(this, QUARTERBACK,qbMoves);
        members = new Professional[]{compSci,president,cbChair,sdCoach,dictator,quarterback};
    }

    //EFFECTS: Builds team from JSONObject data
    public Team(Player leader,JSONObject jsonObject) {
        this.leader = leader;
        this.name = jsonObject.getString("name");
        this.members = membersFromJson(jsonObject.getJSONArray("members"));

    }

    public Professional[] membersFromJson(JSONArray jsonArray) {
        Professional[] professionals = new Professional[6];
        for (int i = 0; i < jsonArray.length(); i++) {
            //Retrieve JSONObject from JSONArray
            JSONObject jsProf = (JSONObject) jsonArray.get(i);
            //Build Professional
            Professional newProf = new Professional(this,jsProf);
            //Add Professional
            professionals[i] = newProf;
        }

        return professionals;
    }



    public void save(int currentIndex) {
        save(currentIndex,leader,"teams");
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("members", membersToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray membersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Professional p : members) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

//--- ACCESSORS & MUTATORS ---------------------------------------------------------------------------------------------
    public Player getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public Professional[] getMembers() {
        return members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(Professional[] members) {
        this.members = members;
    }




}
