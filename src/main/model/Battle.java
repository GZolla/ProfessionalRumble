package model;

import persistence.SaveAble;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.Main;

import java.util.ArrayList;


public class Battle implements SaveAble {
    private final Player player1;
    private final Player player2;
    private ArrayList<Round> rounds;
    private Result result;
    private final int index;

    public enum Result {
        ONGOING,P1WIN,P2WIN
    }

    //EFFECTS: builds a new battle between the given players
    public Battle(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        rounds = new ArrayList<>();
        result = Result.ONGOING;
        index = -1;

    }


    //EFFECTS: creates a battle from JSON data with given index
    public Battle(int index, JSONObject data) {
        this.index = index;
        player1 = new Player(data.getJSONObject("player 1"));
        player2 = new Player(data.getJSONObject("player 2"));
        JSONArray roundData = data.getJSONArray("rounds");
        rounds = new ArrayList<>(roundData.length());
        result = Result.valueOf(data.getString("result"));

        for (Object o : roundData) {
            JSONObject jsonRound = (JSONObject) o;
            int[] actionP1 = new int[]{jsonRound.getInt("P1action"),jsonRound.getInt("P1index")};
            int[] actionP2 = new int[]{jsonRound.getInt("P2action"),jsonRound.getInt("P2index")};
            rounds.add(new Round(this,actionP1,actionP2));
        }

    }



    //EFFECTS: runs all the rounds in battle
    public void logRounds() {
        Main.BATTLEMGR.setDelaying(false);
        ArrayList<Round> rounds = this.rounds;
        this.rounds = new ArrayList<>();
        for (Round r : rounds) {
            this.addRound(r);
            r.handleAll();
        }
    }




    //EFFECTS: Returns the result of a battle as a string
    public String getResultString() {
        switch (result) {
            case P1WIN:
                return player1.getName() + " defeated " + player2.getName() + "!";
            case P2WIN:
                return player2.getName() + " defeated " + player1.getName() + "!";
            default:
                return "Ongoing";
        }
    }

    //MODIFIES: this
    //EFFECTS: adds new round to rounds
    public void addRound(Round newRound) {
        rounds.add(newRound);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }

//--- PERSISTENCE ------------------------------------------------------------------------------------------------------

    //EFFECTS: Saves this to player1's list of battles
    public void save() {
        save(index == -1 ? player1.loadBattles().length : index, player1, "battles");
    }


    @Override
    //EFFECTS: Converts this battle to JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("player 1", player1.toJson());
        json.put("player 2",player2.toJson());

        JSONArray jsonRounds = new JSONArray();
        for (Round r : rounds) {
            jsonRounds.put(r.toJson());
        }
        json.put("rounds", jsonRounds);
        json.put("result",result);

        return json;
    }

    @Override
    public String getName() {
        return player1.getName() + " vs " + player2.getName();
    }


}
