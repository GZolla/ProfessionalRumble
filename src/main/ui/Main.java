package ui;

import model.Player;
import model.Professional;

public class Main {

    public static final Player PLAYER_1 = new Player("Paul");
    public static final Player PLAYER_2 = new Player("John");

    public static void main(String[] args) {
        System.out.println("AAAAAAAA");
    }

    //EFFECT: Return the selected Professional from PLAYER_1(if usedByPlayer1) or PLAYER_2
    public static Professional getUser(boolean usedByPlayer1) {
        return (usedByPlayer1 ? PLAYER_1 : PLAYER_2).getSelectedProfessional();
    }
}
