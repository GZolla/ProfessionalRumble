package model;

import java.util.ArrayList;

public class Battle {
    private Player player1;
    private Player player2;
    private ArrayList<Round> rounds;

    public Battle(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        rounds = new ArrayList<>();
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
}
